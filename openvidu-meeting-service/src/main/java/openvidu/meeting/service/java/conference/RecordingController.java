package openvidu.meeting.service.java.conference;

import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.OpenviduDB;
import openvidu.meeting.service.java.common.dto.BaseResponseBody;
import openvidu.meeting.service.java.conference.dto.request.ConferenceRecStartReqDto;
import openvidu.meeting.service.java.conference.dto.response.RecordingListResDto;
import openvidu.meeting.service.java.conference.entity.Conference;
import openvidu.meeting.service.java.conference.entity.UserRole;
import openvidu.meeting.service.java.conference.repository.ConferenceRepository;
import openvidu.meeting.service.java.conference.service.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recordings")
@CrossOrigin(origins = "*")
public class RecordingController {
    private OpenVidu openvidu;
    private Map<String, Map<String, UserRole>> mapSessionNamesTokens; // <classId, <identification, Role>>
    private Map<String, Boolean> sessionRecordings; // <classId, true/false>
    private Map<String, String> sessionRecordingPerson; // <recordingId, identification>

    private final ConferenceRepository conferenceRepository;


    @PostConstruct
    public void init() throws OpenViduJavaClientException, OpenViduHttpException {
        openvidu = OpenviduDB.getOpenvidu();

        mapSessionNamesTokens = OpenviduDB.getMapSessionNameTokens();
        sessionRecordings = new ConcurrentHashMap<>();

        sessionRecordingPerson = new ConcurrentHashMap<>();
    }

    public boolean isPowerCheck(String classId){
        Map<String, UserRole> entryList = mapSessionNamesTokens.get(classId);

        // 방에 존재하는 사람들이 모두 학생이면 true를 반환하고
        // 한 명이라도 코치, 강사, 컨설턴트, 프로님이 있으면 false를 반환한다.
        for(String key : entryList.keySet()){
            if(entryList.get(key) != UserRole.STUDENT){
                return false;
            }
        }
        return true;
    }


    @PostMapping("/start/{identification}/{role}")
    public ResponseEntity<? extends BaseResponseBody> startRecording(
            @PathVariable(name="identification") String identification,
            @PathVariable(name="role") String role,
            @RequestBody ConferenceRecStartReqDto reqDto) throws OpenViduJavaClientException, OpenViduHttpException{

        RecordingProperties properties = new RecordingProperties.Builder()
                .outputMode(reqDto.getOutputMode())
                .hasAudio(reqDto.isHasAudio())
                .hasVideo(reqDto.isHasVideo()).build();


        try {
            Recording recording = this.openvidu.startRecording(reqDto.getClassId(), properties);

            // 이미 녹화를 진행하고 있는 경우
            if(sessionRecordings.get(reqDto.getClassId()) != null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "녹화를 진행하고 있습니다."));
            }

            // 녹화를 하려는 사람은 학생인데 방 안에 학생이 아닌 컨설턴트or코치or강사or프로님이 있는 경우
            if(role.equals("STUDENT") && !isPowerCheck(reqDto.getClassId()))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(403, "녹화를 할 수 있는 권한이 없습니다."));

            this.sessionRecordings.put(reqDto.getClassId(), true);

            this.sessionRecordingPerson.put(recording.getId(), identification);

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, recording.getId()));
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(406, "녹화를 생성하지 못했습니다."));
        }
    }

    @PostMapping("/stop/{recording_id}")
    public ResponseEntity<? extends BaseResponseBody> stopRecording(@PathVariable(name="recording_id")String recordingId) {
        try {
            Recording recording = this.openvidu.stopRecording(recordingId);
            this.sessionRecordings.remove(recording.getSessionId());
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, recording));
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(407, "녹화를 멈출 수 없습니다. 녹화를 시작하고 있는지 다시 확인하세요."));
        }
    }

    @DeleteMapping("/{recording_id}")
    public ResponseEntity<? extends BaseResponseBody> deleteRecording(@PathVariable(name="recording_id")String recordingId) {
        try {
            this.openvidu.deleteRecording(recordingId);
            this.sessionRecordingPerson.remove(recordingId);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, true));
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(408, "녹화를 삭제할 수 없습니다. 녹화 목록을 다시 확인하세요."));
        }
    }

    @PostMapping("/list/{class_id}/{identification}")
    public ResponseEntity<? extends BaseResponseBody> recordingList(
            @PathVariable(name="class_id") String classId,
            @PathVariable(name="identification") String identification) {
        try {
            List<Recording> recordings = this.openvidu.listRecordings();

            // RecordingListResDto => recordingId, url
            List<RecordingListResDto> resDtoList = new ArrayList<>();

            RecordingListResDto resDto;
            String id;
            for(Recording recording : recordings){
                id = recording.getId();
                if(id.length() >= classId.length() && id.substring(0, classId.length()).equals(classId)){
                    if(sessionRecordingPerson.get(id).equals(identification)){
                        resDto = RecordingListResDto.builder()
                                .title(id)
                                .url(recording.getUrl())
                                .build();
                        resDtoList.add(resDto);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDtoList));
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(409,"녹화를 확인할 수 없습니다."));
        }
    }

    @PostMapping("/one/{recording_id}")
    public ResponseEntity<? extends BaseResponseBody> getRecording(@PathVariable(name = "recording_id")String recordingId){
        try{
            Recording recording = this.openvidu.getRecording(recordingId);
            RecordingListResDto resDto = RecordingListResDto.builder()
                    .title(recording.getId())
                    .url(recording.getUrl())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, resDto));
        }catch(OpenViduJavaClientException | OpenViduHttpException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(409, "녹화를 확인할 수 없습니다."));
        }
    }
}
