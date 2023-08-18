package openvidu.meeting.service.java.conference;


import io.swagger.annotations.*;
import openvidu.meeting.service.java.OpenviduDB;
import openvidu.meeting.service.java.common.dto.BaseResponseBody;
import openvidu.meeting.service.java.conference.dto.request.ConferenceCreateReqDto;
import openvidu.meeting.service.java.conference.dto.response.ConferenceCreateResDto;
import openvidu.meeting.service.java.conference.dto.response.ConferenceDetailResDto;
import openvidu.meeting.service.java.conference.dto.response.ConferenceHostListResDto;
import openvidu.meeting.service.java.conference.entity.Conference;
import openvidu.meeting.service.java.conference.repository.ConferenceRepository;
import openvidu.meeting.service.java.conference.service.ConferenceService;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.conference.streaming.VideoRecorder;
import openvidu.meeting.service.java.conference.streaming.VideoSender;
import openvidu.meeting.service.java.conference.streaming.ZipFileDownloader;
import openvidu.meeting.service.java.exception.ExceptionEnum;
import openvidu.meeting.service.java.history.dto.request.HistoryReqDto;
import openvidu.meeting.service.java.history.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Api(value = "Conference Management")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/conferences")
@CrossOrigin(origins = "*")
public class ConferenceController {

    private Logger logger = LoggerFactory.getLogger(ConferenceController.class);
    private OpenVidu openvidu;

    @Value("${spring.connection.path}")
    private String root;
    //private Map<String, Map<String, String>> sessionConnectionList; // classId, <identification, connectionId>
    private Map<String, List<String>> sessionParticipantList; // <classId, [participant Name1, Name2, ... ]>
    private Map<String, String> sessionHostList; // <classId, identification>
    private Map<String, VideoRecorder> currentRecordingList; // <classId, VideoRecorder>

    // 스레드 관련 변수
    private ExecutorService executorService;

    // 녹화 삭제 관련 변수
    private ZipFileDownloader zipFileDownloader;

    // history
    private final HistoryService historyService;

    private final ConferenceRepository conferenceRepository;

    private final ConferenceService conferenceService;

    @Value("${local.recording.path}")
    private String recordingFilePath;
    // i9d107.p.ssafy.io
    private String meetingUrl = "http://localhost:3000/meeting/";

    @PostConstruct
    public void init() throws OpenViduJavaClientException, OpenViduHttpException, IOException {
        openvidu = OpenviduDB.getOpenvidu();
        sessionHostList = new ConcurrentHashMap<>();
        sessionParticipantList = new ConcurrentHashMap<>();

        // 각 세션마다 있는 녹화기능을 저장한다.
        currentRecordingList = new ConcurrentHashMap<>();

        // 스레드 관련 초기화(다른 방(세션)에서도 녹화 기능을 사용할 수 있도록 멀티스레드로 구현)
        executorService = Executors.newCachedThreadPool();

        // 녹화 삭제 관련 초기화
        zipFileDownloader = new ZipFileDownloader(new RestTemplateBuilder());

        // DB에 있는 conference 데이터를 Openvidu에 넣어준다.
        conferenceSetting();

        // 로컬에 폴더를 생성한다.
        fileOrDirectorySetting();

        // 이전에 남아있는 녹화 기록을 모두 지운다
        List<Recording> recordings = this.openvidu.listRecordings();
        for(Recording rec : recordings){
            try{
                this.openvidu.stopRecording(rec.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
            this.openvidu.deleteRecording(rec.getId());
        }


    }


    // DB에 있는 방(세션)을 모두 오픈비두에 넣어준다.
    public void conferenceSetting() throws OpenViduJavaClientException, OpenViduHttpException {
        List<Conference> roomList = conferenceRepository.findAll();

        SessionProperties properties;
        Session session;
        for (Conference conference : roomList) {
            properties = new SessionProperties.Builder()
                    .customSessionId(conference.getClassId())
                    .mediaMode(MediaMode.ROUTED)
                    .build();

            session = openvidu.createSession(properties);
            // 방의 호스트를 저장한다.
            sessionHostList.put(conference.getClassId(), conference.getIdentification());
        }
    }

    public void fileOrDirectorySetting(){
        Path recordingPath, totalTextFile, totalZipFile;
        try{
            if(Files.isDirectory(Paths.get(recordingFilePath+"RecordingFile"))){
                zipFileDownloader.removeFolder(recordingFilePath,"RecordingFile", true);
            }

            if(Files.isDirectory(Paths.get(recordingFilePath+"TotalTextFile"))){
                zipFileDownloader.removeFolder(recordingFilePath,"TotalTextFile", true);
            }

            if(Files.isDirectory(Paths.get(recordingFilePath+"TotalZipFile"))){
                zipFileDownloader.removeFolder(recordingFilePath,"TotalZipFile", true);
            }


            recordingPath = Files.createDirectories(Paths.get(recordingFilePath).resolve("RecordingFile"));
            totalTextFile = Files.createDirectories(Paths.get(recordingFilePath).resolve("TotalTextFile"));
            totalZipFile = Files.createDirectories(Paths.get(recordingFilePath).resolve("TotalZipFile"));

            logger.info("로컬에 RecordingFile, TotalTextFile, TotalZipFile 폴더를 생성함");
        }catch(Exception e){
            logger.info("로컬에 RecordingFile, TotalTextFile, TotalZipFile 폴더를 생성하지 못했음");
        }
    }



    //사용자가 방을 생성한다.
    @ApiOperation(value = "사용자가 방을 생성한다.", notes = "새로운 회의방 만들고 회의 URL반환")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BaseResponseBody.class),
            @ApiResponse(code = 4009, message = "404 Error")
    })
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody> createConference(
            @ApiParam(value = "회의 세부 사항", required = true)
            @RequestBody(required = false) ConferenceCreateReqDto reqDto)
            throws OpenViduJavaClientException, OpenViduHttpException, IOException, InterruptedException {

        // 이미 만들어진 방(세션)인 경우 → 기존에 있던 방의 url을 반환한다.
        if (conferenceRepository.findByClassId((String) reqDto.getClassId()) != null) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, root+ reqDto.getClassId()));
        }


        // Openvidu에서 방(세션)을 생성함
        try {
            SessionProperties properties = new SessionProperties.Builder()
                    .customSessionId(reqDto.getClassId())
                    .mediaMode(MediaMode.ROUTED)
                    .build();

            Session session = openvidu.createSession(properties);

            // DB에 방(세션)을 저장함
            ConferenceCreateResDto resDto = ConferenceCreateResDto.builder()
                    .identification(reqDto.getIdentification())
                    .classId(reqDto.getClassId()).title(reqDto.getTitle())
                    .conferenceUrl(meetingUrl + reqDto.getClassId())
                    .active(reqDto.isActive()).build();

            // 새롭게 생성한 방을 DB에 저장한다.
            Conference newConference = conferenceService.createSession(resDto);

            // 호스트 설정
            sessionHostList.put((String) reqDto.getClassId(), (String)reqDto.getIdentification());

            //history save
            HistoryReqDto dto = new HistoryReqDto();
            dto.setConference_id(newConference.getId());
            dto.setIdentification(newConference.getIdentification());
            historyService.createHistory(dto, "CREATE");

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDto.getConferenceUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // 사용자가 HOST인 방 리스트 가져오기(방 이름, 설명, url을 반환함)
    @ApiOperation(value = "사용자가 HOST인 활성화가 되어있는 방 리스트 가져오기", notes = "지정된 ID를 기준으로 회의 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BaseResponseBody.class),
            @ApiResponse(code = 4009, message = "404 Error")
    })
    @GetMapping
    public ResponseEntity<? extends BaseResponseBody> conferenceList(
            @ApiParam(value = "해당 회의 목록을 가져오는 사용자 identification", required = true)
            @RequestParam(name = "identification") String identification) {
        try {
            List<Conference> roomList = conferenceRepository.findAllByIdentificationAndActiveTrue(identification);

            List<ConferenceHostListResDto> dtoList = roomList.stream()
                    .map(conference -> new ConferenceHostListResDto(conference.getConferenceUrl(), conference.getTitle()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, dtoList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    //  사람이 방(세션)에 들어갈 때(방이 존재하는지 확인하고, 토큰을 발급해준다)
    @ApiOperation(value = "방에 사용자가 들어가는 경우(방 연결)", notes = "class_id, identification, role을 기반으로 특정 회의방에 사용자를 연결")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BaseResponseBody.class),
            @ApiResponse(code = 4001, message = "아직 host가 방을 활성화하지 않았습니다. 기다려주세요."),
            @ApiResponse(code = 404, message = "404 ERROR")
    })

    @PostMapping("/connection/{class_id}/{identification}")
    public ResponseEntity<String> connectionConference(
            HttpServletRequest request,
            @ApiParam(value = "연결할 컨퍼런스의 ID", required = true)
            @PathVariable("class_id") String classId,

            @ApiParam(value = "사용자 identification", required = true)
            @PathVariable("identification") String identification,

            @ApiParam(value = "connection에 필요한 설정 map", required = false)
            @RequestBody(required = false) Map<String, Object> info) throws OpenViduJavaClientException, OpenViduHttpException
    {

        Session session = openvidu.getActiveSession(classId);


        // 존재하지 않는 방인 경우
        if (session == null) {
            return new ResponseEntity<>("방이 없음", HttpStatus.NOT_FOUND);
        }


        boolean firstCome = false;
        if(!sessionParticipantList.containsKey(classId)){
            // 방에 처음 들어오는 사람이 host가 아닌 경우
            if(!identification.equals(sessionHostList.get(classId))){
                logger.info("방에 처음 들어오는 사람이 host가 아닌 경우 : "+identification+","+sessionHostList.get(classId));
                return new ResponseEntity<>("아직 host가 방을 활성화하지 않았습니다. 기다려주세요.", HttpStatus.NOT_FOUND);
            }
            firstCome = true;
        }


        try {
            // 연결 설정
            ConnectionProperties properties = ConnectionProperties.fromJson(info).build();
            Connection connection = session.createConnection(properties);

            // 방에 제일 처음 입장하는 경우(host인 경우)
            if (firstCome) {
                logger.info("처음 입장합니다");

                // 방에 참가한 사람들을 담을 map을 세팅한다.
                sessionParticipantList.put(classId, new ArrayList<>());
                OpenviduDB.getSessionConnectionList().put(classId, new HashMap<>());

                // host의 connectionId를 저장한다
                OpenviduDB.getHostConnectionId().put(classId, connection.getConnectionId());

                // accessToken을 받아서 저장한다.
                String accessToken = request.getHeader("Authorization");
                OpenviduDB.getHostToken().put(identification, accessToken);
                logger.info(identification+"////-> "+ accessToken);

                // 녹화를 시작한다.
                currentRecordingList.put(classId, new VideoRecorder(classId, identification));

                // 해당 세션을 스레드로 시작한다.
                executorService.submit(() -> currentRecordingList.get(classId).recordingMethod());

            }

            logger.info("userInfo : "+ classId+","+identification);

            // 어디 방에 들어간 사람인지 구분하기 위함(참가자 리스트에 추가함)
            sessionParticipantList.get(classId).add(identification);

            // connectionId 저장(identification, connectionId를 추가함)
            OpenviduDB.getSessionConnectionList().get(classId).put(identification, connection.getConnectionId());

            //history connection
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(identification);
            historyService.createHistory(dto, "CONNECTION");

            return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("404 ERROR", HttpStatus.NOT_FOUND);
        }
    }

    // 사용자가 방을 나가는 경우 => connection에서 삭제, participant 비우고 host 에서도 삭제
    @ApiOperation(value = "방 연결 해제", notes = "제공된 class ID와 identification을 기반으로 특정 회의에서 사용자 연결 해제")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BaseResponseBody.class),
            @ApiResponse(code = 4001, message = "호스트가 방을 삭제하지 못했습니다."),
            @ApiResponse(code = 4002, message = "HISTORY ERROR")
    })
    // 호스트 : 회의 종료, 사용자 : 회의 나가기
    @PostMapping("/{class_id}/{identification}")
    public ResponseEntity<? extends BaseResponseBody> disconnectionConference(
            @ApiParam(value = "연결 해제할 회의의 ID", required = true)
            @PathVariable("class_id") String classId,

            @ApiParam(value = "연결 해제를 위한 사용자 식별 정보", required = true)
            @PathVariable("identification") String identification)

            throws OpenViduJavaClientException, OpenViduHttpException
    {

        // Host가 회의 종료를 누른 경우
        if(identification.equals(sessionHostList.get(classId))){
            try {
                // 회의에 참가하고 있는 사용자 전체 삭제
                sessionParticipantList.remove(classId);

                // connectionId 삭제
                OpenviduDB.getHostConnectionId().remove(classId);

                // 호스트 토큰 삭제
                OpenviduDB.getHostToken().remove(classId);

                // openvidu에서 session과 연결되어있는 connection을 모두 삭제함
                Session session = openvidu.getActiveSession(classId);
                for(String conId : OpenviduDB.getSessionConnectionList().get(classId).keySet()){
                    session.forceDisconnect(conId);
                }

                // 회의에 참가하고 있는 connectionId 전부 삭제
                OpenviduDB.getSessionConnectionList().remove(classId);

                // videoRecorder 연결 끊기
                currentRecordingList.get(classId).recordingStop();
                currentRecordingList.remove(classId);

                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "호스트가 회의를 종료했습니다."));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4001, ExceptionEnum.CONFERENCE_HOST_NOT_TERMINATED));
            }
        }

        // 사용자가 회의 종료를 누른 경우
        for (String id : sessionParticipantList.get(classId)) {
            if (id.equals(identification)) {
                // 참가자 목록에서 삭제함
                sessionParticipantList.remove(id);

                // openvidu와 연결을 해제
                String connectionId = OpenviduDB.getSessionConnectionList().get(classId).get(identification);
                openvidu.getActiveSession(classId).forceDisconnect(connectionId);


                // sessionConnectionList에서 삭제함
                OpenviduDB.getSessionConnectionList().get(classId).remove(identification);
                break;
            }
        }

        try {
            //history exit/leave
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(identification);
            historyService.createHistory(dto, "EXIT");
            //방을 나갔는데 모두 나가게 되어서 LEAVE
            if(OpenviduDB.getSessionConnectionList().get(classId).size() == 0){
                historyService.createHistory(dto,"LEAVE");
            }
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "사용자가 회의를 종료했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4002, ExceptionEnum.CONFERENCE_HISTORY_FAILED));
        }
    }

    // 회의 상세 보기(1개)
    // title, active, updatedAt을 반환한다.
    @ApiOperation(value = "회의 상세 정보 조회", notes = "제공된 class ID를 기반으로 특정 회의의 상세 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BaseResponseBody.class),
            @ApiResponse(code = 4009, message = "404 ERROR")
    })
    @GetMapping("/{class_id}")
    public ResponseEntity<? extends BaseResponseBody> conferenceDetail(
            @ApiParam(value = "상세 정보를 조회할 회의의 ID", required = true)
            @PathVariable(name = "class_id") String classId)
    {
        try {
            Conference conference = conferenceRepository.findByClassId(classId);
            ConferenceDetailResDto resDto = ConferenceDetailResDto.builder()
                    .title(conference.getTitle())
                    .active(conference.isActive())
                    .updatedAt(conference.getUpdatedAt()).build();
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // 회의 수정 (제목)
    @ApiOperation(value = "회의 상세 정보 수정", notes = "제공된 class ID 및 정보를 기반으로 특정 회의의 상세 정보를 업데이트")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BaseResponseBody.class),
            @ApiResponse(code = 4009, message = "404 ERROR")
    })
    @PatchMapping("/{class_id}")
    public ResponseEntity<? extends BaseResponseBody> modifyConference(
            @ApiParam(value = "수정할 회의의 ID", required = true)
            @PathVariable(name = "class_id") String classId,

            @ApiParam(value = "회의에 대한 업데이트 정보", required = true)
            @RequestBody(required = false) Map<String, Object> info)
    {
        try {
            String title = (String) info.get("title");

            Conference conference = conferenceRepository.findByClassId(classId);
            conference.setTitle(title);

            conferenceRepository.save(conference);

            ConferenceDetailResDto resDto = ConferenceDetailResDto.builder()
                    .title(conference.getTitle())
                    .updatedAt(conference.getUpdatedAt()).build();

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // 회의 비활성화(삭제)
    @ApiOperation(value = "회의 비활성화",
            notes = " 회의의 호스트만 비활성화 가능")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 4003, message = "방은 호스트만 삭제할 수 있습니다."),
            @ApiResponse(code = 4009, message = "404 ERROR")
    })
    @PatchMapping("/{class_id}/status")
    public ResponseEntity<? extends BaseResponseBody> disableConference(
            @ApiParam(value = "ID of the class or conference to be modified", required = true)
            @PathVariable(name = "class_id") String classId,
            @ApiParam(value = "Identification of the person requesting the status change", required = true)
            @RequestParam(name = "identification") String identification)
    {
        // 방을 삭제하려는 사람이 Host가 아닌 경우
        if (!identification.equals(sessionHostList.get(classId))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4003, ExceptionEnum.ONLY_HOST_CAN_DELETE));
        }

        // 방을 삭제하려는 사람이 Host인 경우
        try {
            Conference conference = conferenceRepository.findByClassId(classId);

            // DB에 방을 비활성화
            conference.setActive(false);
            conferenceRepository.save(conference);

            // 녹화를 중지하기
//            VideoRecorder videoRecorder = currentRecordingList.get(classId);
//            videoRecorder.recordingStop();

            // 로컬의 녹화 파일들 삭제하기
            // 로컬의 녹화 파일들 삭제하기
//            zipFileDownloader.removeFolder(recordingFilePath,classId,false);

            //history DELETE
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(nowConference.getIdentification());
            historyService.createHistory(dto, "DELETE");

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "비활성화 되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // User가 방문한 회의 리스트 조회 (최근 6개)
    @ApiOperation(value = "사용자가 방문한 최근 회의 목록 조회",
            notes = "사용자가 방문한 가장 최근의 6개 회의를 반환")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 4009, message = "404 ERROR")
    })
    @GetMapping("/visited")
    public ResponseEntity<? extends BaseResponseBody> recentConference(
            @ApiParam(value = "최근 회의를 조회할 사용자의 식별자 identification", required = true)
            @RequestParam(name = "identification") String identification)
    {
        try {
            Page<Conference> page = conferenceService.recentConference(identification);
            List<ConferenceHostListResDto> resultList = page.getContent()
                    .stream()
                    .map(conference -> new ConferenceHostListResDto(conference.getConferenceUrl(), conference.getTitle()))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resultList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // Host가 녹화 중지하기
    @ApiOperation(value = "HOST가 녹화를 중지한 경우",
            notes = "호스트가 지정된 회의의 녹화를 중지")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 4004, message = "HOST가 녹화를 중지할 수 없습니다.")
    })
    @GetMapping("/stop/recordings/{class_id}")
    public ResponseEntity<? extends BaseResponseBody> stopRecording(
            @ApiParam(value = "녹화를 중지할 회의의 식별자 class_id", required = true)
            @PathVariable(name = "class_id") String classId) throws IOException, OpenViduJavaClientException, OpenViduHttpException {
        try{
//            List<Recording> list = this.openvidu.listRecordings();
//            for(Recording rec : list){
//                this.openvidu.deleteRecording(rec.getId());
//            }

            // 녹화를 중지하기
            VideoRecorder videoRecorder = currentRecordingList.get(classId);
            videoRecorder.recordingStop();

            // 로컬의 녹화 파일들 삭제하기
            //zipFileDownloader.removeFolder(recordingFilePath,classId,false);

            // 녹화 기능을 목록에서 삭제한다.
            currentRecordingList.remove(classId);

            // 이전의 녹화기록 삭제하기
//        List<Recording> list = this.openvidu.listRecordings();
//        for(Recording rec : list){
//            this.openvidu.deleteRecording(rec.getId());
//        }
//

            logger.info("녹화를 중지하고 삭제함");

            //history REC_END
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(nowConference.getIdentification());
            historyService.createHistory(dto, "REC_END");
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "성공!!!"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4004, ExceptionEnum.CONFERENCE_HOST_RECORDING_NOT_TERMINATED));
        }


    }


    // 회의에 참가하고 있는 사람들 목록 가져오기(identification을 반환함)
    @ApiOperation(value = "회의 참가자 목록 조회",
            notes = "지정된 회의에 현재 참가하고 있는 참가자들의 식별자 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 4009, message = "404 ERROR"),
    })
    @GetMapping("/enter/{class_id}")
    public ResponseEntity<? extends BaseResponseBody> conferenceEntryList(
            @ApiParam(value = "참가자를 조회할 회의의 식별자", required = true)
            @PathVariable("class_id") String classId)
    {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, OpenviduDB.getSessionConnectionList().get(classId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }

    }

    // HOST가 녹화 시작을 눌렀을 때
    @ApiOperation(value = "HOST가 녹화를 시작한 경우",
            notes = "지정된 회의에 현재 참가하고 있는 참가자들의 식별자 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 4005, message = "HOST가 녹화를 생성할 수 없습니다."),
    })
    @GetMapping("/start/recording/{class_id}")
    public ResponseEntity<? extends BaseResponseBody> startRecording(
            @ApiParam(value = "녹화를 중지할 회의의 식별자 class_id", required = true)
            @PathVariable(name="class_id") String classId) throws IOException {

        try{
            //history REC_START
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(nowConference.getIdentification());
            historyService.createHistory(dto, "REC_START");

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "녹화를 시작합니다."));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4005, ExceptionEnum.CONFERENCE_HOST_RECORDING_NOT_CREATE));
        }

    }

}
