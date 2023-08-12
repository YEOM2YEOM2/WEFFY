package openvidu.meeting.service.java.conference;

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
import openvidu.meeting.service.java.conference.streaming.ZipFileDownloader;
import openvidu.meeting.service.java.exception.ExceptionEnum;
import openvidu.meeting.service.java.history.dto.request.HistoryReqDto;
import openvidu.meeting.service.java.history.service.HistoryService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/conferences")
@CrossOrigin(origins = "*")
public class ConferenceController {
    private OpenVidu openvidu;
    private Map<String, String> hostToken;
    private String root = "http://localhost:8080/";
    private Map<String, Map<String, String>> sessionConnectionList; // classId, <identification, connectionId>
    private Map<String, List<String>> sessionParticipantList;
    private Map<String, String> sessionHostList;
    private Map<String, VideoRecorder> currentRecordingList;

    // 스레드 관련 변수
    private ExecutorService executorService;

    // 녹화 삭제 관련 변수
    private ZipFileDownloader zipFileDownloader;

    private final HistoryService historyService;

    private final ConferenceRepository conferenceRepository;

    private final ConferenceService conferenceService;

    @PostConstruct
    public void init() throws OpenViduJavaClientException, OpenViduHttpException {
        openvidu = OpenviduDB.getOpenvidu();

        sessionConnectionList = new ConcurrentHashMap<>();
        sessionHostList = new ConcurrentHashMap<>();
        sessionParticipantList = new ConcurrentHashMap<>();

        // 각 세션마다 있는 녹화기능을 저장한다.
        currentRecordingList = new ConcurrentHashMap<>();

        // 스레드 관련 초기화(다른 방(세션)에서도 녹화 기능을 사용할 수 있도록 멀티스레드로 구현했다)
        executorService = Executors.newCachedThreadPool();

        // 녹화 삭제 관련 초기화
        zipFileDownloader = new ZipFileDownloader(new RestTemplateBuilder());

        // 각 방의 호스트가 가지고 있는 세션을 저장한다.
        hostToken = OpenviduDB.getHostToken();

        conferenceSetting();
    }

    // DB에 있는 방(세션)을 모두 오픈비두에 넣어준다.
    public void conferenceSetting() throws OpenViduJavaClientException, OpenViduHttpException {
        List<Conference> roomList = conferenceRepository.findAll();

        SessionProperties properties;
        Session session;
        for(Conference conference : roomList){
            properties = new SessionProperties.Builder().customSessionId(conference.getClassId()).build();
            session = openvidu.createSession(properties);

            // 기존에 있던 방을 방 참가자 리스트에 추가한다.
            sessionParticipantList.put(conference.getClassId(), new ArrayList<>());
        }
    }

    //방 생성
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody>createConference(@RequestBody(required = false) ConferenceCreateReqDto reqDto)
            throws OpenViduJavaClientException , OpenViduHttpException{

        // 이미 만들어진 방(세션)인 경우
        if(conferenceRepository.findByClassId((String)reqDto.getClassId()) != null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4000, ExceptionEnum.CONFERENCE_EXIST));
        }

        try{
            // openvidu에서 방(세션)을 생성함
            SessionProperties properties = new SessionProperties.Builder().customSessionId(reqDto.getClassId()).build();
            Session session = openvidu.createSession(properties);

            // DB에 방(세션)을 저장함
            ConferenceCreateResDto resDto = ConferenceCreateResDto.builder()
                    .identification(reqDto.getIdentification())
                    .classId(reqDto.getClassId()).title(reqDto.getTitle())
                    .description(reqDto.getDescription())
                    .conferenceUrl(root+reqDto.getClassId())
                    .active(reqDto.isActive()).build();

            // 새롭게 생성한 방을 DB에 저장한다.
            Conference newConference = conferenceService.createSession(resDto);

            // 새롭게 생성한 방을 방 참가자 리스트에 추가한다.
            sessionParticipantList.put(reqDto.getClassId(), new ArrayList<>());

            // 방에 참가한 사람들을 담을 map을 초기화한다.
            sessionConnectionList.put((String)reqDto.getClassId(), new HashMap<>());

            //history save
            HistoryReqDto dto = new HistoryReqDto();
            dto.setConference_id(newConference.getId());
            dto.setIdentification(newConference.getIdentification());
            historyService.createHistory(dto,"CREATE");

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDto.getConferenceUrl()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // 유저가 host인 방 리스트 가져오기(방 이름, 설명, url을 반환함)
    @GetMapping
    public ResponseEntity<? extends BaseResponseBody>conferenceList(@RequestParam(name = "identification") String identification) {
        try{
            List<Conference> roomList = conferenceRepository.findAllByIdentification(identification);

            List<ConferenceHostListResDto> dtoList = roomList.stream()
                    .map(conference -> new ConferenceHostListResDto(conference.getTitle(), conference.getDescription(), conference.getConferenceUrl()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, dtoList));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    //  사람이 방(세션)에 들어갈 때(방이 존재하는지 확인하고, 토큰을 발급해준다)
    @PostMapping("/{class_id}")
    public ResponseEntity<? extends BaseResponseBody>connectionConference(HttpServletResponse response,
                                                                          @PathVariable("class_id") String classId,
                                                                          @RequestParam("identification") String identification, @RequestParam("role") String role,
                                                                          @RequestBody(required = false) Map<String, Object> info) {

        Session session = openvidu.getActiveSession(classId);

        // 존재하지 않는 방인 경우
        if(session == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4001, ExceptionEnum.CONFERENCE_NOT_EXIST));
        }

        try{
            // 연결 설정
            ConnectionProperties properties = ConnectionProperties.fromJson(info).build();
            Connection connection = session.createConnection(properties);

            // connectionId 저장
            sessionConnectionList.get(classId).put(identification, connection.getConnectionId());

            // 방에 제일 처음 입장하는 경우
            if(sessionParticipantList.get(classId).size() == 0){
                System.out.println("처음 입장합니다");

                // 호스트 설정
                sessionHostList.put(classId, identification);

                // accessToken을 받아서 저장한다.
                String accessToken = response.getHeader("Authorization");
                hostToken.put(identification, accessToken);

                // 녹화를 시작한다.
                currentRecordingList.put(classId, new VideoRecorder(classId, identification));

                // 해당 세션을 스레드로 시작한다.
                executorService.submit(() -> currentRecordingList.get(classId).recordingMethod());
            }

            // 어디 방에 들어간 사람인지 구분하기 위함
            sessionParticipantList.get(classId).add(identification);

            //history connection
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(identification);
            historyService.createHistory(dto,"CONNECTION");

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, connection.getConnectionId()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // 사용자가 방을 나가는 경우 => connection에서 삭제, participant 비우고 host 에서도 삭제
    @PostMapping("/{class_id}/{identification}")
    public ResponseEntity<? extends BaseResponseBody>disconnectionConference(@PathVariable("class_id") String classId,
                                                                             @PathVariable("identification") String identification) throws OpenViduJavaClientException, OpenViduHttpException {

        // 방(세션)이 없는 경우
        if(!sessionHostList.containsKey(classId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4001, ExceptionEnum.CONFERENCE_NOT_EXIST));

        boolean ischeck = false;


        for(String id : sessionParticipantList.get(classId)){
                if(id.equals(identification)){

                    // 참가자 목록에서 삭제함
                    sessionParticipantList.remove(id);

                    // openvidu와 연결을 해제
                    String connectionId = sessionConnectionList.get(classId).get(identification);
                    openvidu.getActiveSession(classId).forceDisconnect(connectionId);

                    // sessionConnectionList에서 삭제함
                    sessionConnectionList.remove(identification);

                    ischeck = true;
                    break;
                }
        }
        // 참가하지 않은 참가자인 경우
        if(!ischeck){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4002, ExceptionEnum.CONFERENCE_NOT_PARTICIPATED));
        }


        try{

            //history exit/leave
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(identification);
            historyService.createHistory(dto,"EXIT");
            //방을 나갔는데 모두 나가게 되어서 LEAVE
//            if(mapSessionNamesTokens.get(classId).size() == 0){
//                historyService.createHistory(dto,"LEAVE");
//
//            }

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "퇴장합니다."));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // 회의 상세 보기(1개)
    // title, description, active, updatedAt을 반환한다.
    @GetMapping("/{class_id}")
    public ResponseEntity<? extends BaseResponseBody>conferenceDetail(@PathVariable(name="class_id")String classId) {
        try{
            Conference conference = conferenceRepository.findByClassId(classId);
            ConferenceDetailResDto resDto = ConferenceDetailResDto.builder()
                    .title(conference.getTitle())
                    .description(conference.getDescription())
                    .active(conference.isActive())
                    .updatedAt(conference.getUpdatedAt()).build();
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDto));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // 회의 수정 (제목, 내용)
    @PatchMapping("/{class_id}")
    public ResponseEntity<? extends BaseResponseBody>modifyConference(@PathVariable(name="class_id")String classId,
                                                                      @RequestBody(required = false) Map<String, Object> info) {
        try{
            String title = (String)info.get("title");
            String description = (String)info.get("description");

            Conference conference = conferenceRepository.findByClassId(classId);
            conference.setTitle(title);
            conference.setDescription(description);

            conferenceRepository.save(conference);

            ConferenceDetailResDto resDto = ConferenceDetailResDto.builder()
                    .title(conference.getTitle())
                    .description(conference.getDescription())
                    .updatedAt(conference.getUpdatedAt()).build();

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDto));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // Host가 회의를 종료하는 경우
    @GetMapping("/{class_id}/{identification}/leave")
    public ResponseEntity<? extends BaseResponseBody>leaveConference(@PathVariable(name = "class_id")String classId,
                                                                     @RequestParam(name = "identification")String identification) throws OpenViduJavaClientException, OpenViduHttpException {

        try{
            // 회의에 참가하고 있는 사용자 전체 삭제
            sessionParticipantList.remove(classId);

            // 회의에 참가하고 있는 connectionId 전부 삭제
            sessionConnectionList.remove(classId);

            // 회의 호스트 삭제
            sessionHostList.remove(classId);

            // 호스트 토큰 삭제
            hostToken.remove(classId);

            // openvidu에서 session을 삭제함(이때 session과 연결된 connection은 자동으로 삭제함)
            openvidu.getActiveSessions().remove(classId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(000000, "회의를 종료하지 못했습니다."));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "회의를 종료했습니다."));
        }


    }

    // 회의 비활성화
    @PatchMapping("/{class_id}/status")
    public ResponseEntity<? extends BaseResponseBody>disableConference(@PathVariable(name = "class_id") String classId,
                                                                      @RequestParam(name = "active") boolean active,
                                                                       @RequestParam(name = "identification") String identification) {

        // 방을 삭제하려는 사람이 Host가 아닌 경우
        if(!identification.equals(sessionHostList.get(classId))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4010, ExceptionEnum.ONLY_HOST_DELETE));
        }

        try{
            Conference conference = conferenceRepository.findByClassId(classId);

            // DB에 방을 비활성화
            conference.setActive(active);
            conferenceRepository.save(conference);

            // 로컬의 녹화 파일들 삭제하기
            zipFileDownloader.removeFolder(classId);

            //history DELETE
            HistoryReqDto dto = new HistoryReqDto();
            Conference nowConference = conferenceRepository.findByClassId(classId);
            dto.setConference_id(nowConference.getId());
            dto.setIdentification(nowConference.getIdentification());
            historyService.createHistory(dto,"DELETE");

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "비활성화 되었습니다."));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }
    // User가 방문한 회의 리스트 조회 (최근 10개)
    @GetMapping("/visited")
    public ResponseEntity<? extends  BaseResponseBody>recentConference(@RequestParam(name="identification") String identification) {
        try{
            Page<Conference> page = conferenceService.recentConference(identification);
            List<ConferenceHostListResDto> resultList = page.getContent()
                    .stream()
                    .map(conference -> new ConferenceHostListResDto(conference.getConferenceUrl(), conference.getTitle(), conference.getDescription()))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resultList));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }
    }

    // Host가 녹화 중지하기
    @PostMapping("/recordings/{class_id}/stop")
    public ResponseEntity<? extends BaseResponseBody>stopRecording(@PathVariable(name="class_id") String classId){
        VideoRecorder videoRecorder = currentRecordingList.get(classId);

        // Host가 녹화를 중지한다.
        videoRecorder.recordingStop();

        // 녹화 기능을 목록에서 삭제한다.
        currentRecordingList.remove(classId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "성공!!!"));
    }


    // 회의에 참가하고 있는 사람들 목록 가져오기(identification을 반환함)
    @GetMapping("/enter/{class_id}")
    public ResponseEntity<? extends BaseResponseBody>conferenceEntryList(@PathVariable("class_id")String classId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, sessionConnectionList.get(classId)));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
        }

//        List<String> userList;
//        Map<String, UserRole> map;
//
//        try{
//            userList = new ArrayList<>();
//            map = mapSessionNamesTokens.get(classId);
//            for(String key : map.keySet()){
//                userList.add(key);
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, userList));
//        }catch(Exception e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(4009, ExceptionEnum.GENERIC_ERROR));
//        }
    }


}
