package io.openvidu.basic.java.conference;

import io.openvidu.basic.java.OpenviduDB;
import io.openvidu.basic.java.common.BaseResponseBody;
import io.openvidu.basic.java.conference.dto.request.ConferenceDto;
import io.openvidu.basic.java.conference.entity.Conference;
import io.openvidu.basic.java.conference.repository.ConferenceRepository;
import io.openvidu.basic.java.conference.service.ConferenceService;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/conferences")
@CrossOrigin(origins = "*")
public class ConferenceController {
    private OpenVidu openvidu;
    private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens; // <sessionId, <token, role>>
    private Map<String, Boolean> sessionRecordings;

    private final ConferenceRepository conferenceRepository;

    private final ConferenceService conferenceService;

    @PostConstruct
    public void init() {
        openvidu = OpenviduDB.getOpenvidu();

        mapSessionNamesTokens = OpenviduDB.getMapSessionNameTokens();
        sessionRecordings = OpenviduDB.getSessionRecordings();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<? extends BaseResponseBody>createConference(@RequestBody(required = false) Map<String,Object> info)
            throws OpenViduJavaClientException, OpenViduHttpException{

        // 이미 만들어진 방(세션)인 경우
        if(conferenceRepository.findByClassId((String)info.get("class_id")) != null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(200, null));
        }

        // openvidu에서 방(세션)을 생성함
        SessionProperties properties = new SessionProperties.Builder().customSessionId((String)info.get("class_id")).build();
        Session session = openvidu.createSession(properties);

        // DB에 방(세션)을 저장함
        ConferenceDto dto = ConferenceDto.builder()
                        .ownerId((String)info.get("owner_id")).conference_url((String)info.get("conference_url"))
                        .classId((String)info.get("class_id")).title((String)info.get("title"))
                        .description((String)info.get("description"))
                .active((boolean)info.get("active")).build();

        conferenceService.createSession(dto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, session));
    }

    // 유저가 host인 방 리스트 가져오기
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<? extends BaseResponseBody>conferenceList(@RequestParam(name = "owner_id") String ownerId)
            throws OpenViduJavaClientException, OpenViduHttpException{
        List<Conference> roomList = conferenceRepository.findAllByOwnerId(ownerId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(202, roomList));
    }

    // 사람이 방(세션)에 들어갈 때(방이 존재하는지 확인하고, 토큰을 발급해준다)
    @RequestMapping(value="/{class_id}", method = RequestMethod.POST)
    public ResponseEntity<? extends BaseResponseBody>conferenceConnection(@PathVariable("class_id") String classId,
                                                                          @RequestBody(required = false) Map<String, Object> info)
            throws OpenViduJavaClientException, OpenViduHttpException{
        Session session = openvidu.getActiveSession(classId);

        // 활성화가 되어있는 방이 없음
        if(session == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, null));
        }

        ConnectionProperties properties = ConnectionProperties.fromJson(info).build();

        Connection connection = session.createConnection(properties);

        // 토큰 관리 저장소에 데이터를 넣음(SessionId, Token, Role)
      //  mapSessionNamesTokens.get(classId).put(connection.getToken(), OpenViduRole.valueOf("STUDENT"));

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, connection.getToken()));
    }

    // 회의 상세 보기(1개)
    @GetMapping("/{class_id}")
    public ResponseEntity<? extends BaseResponseBody>detailConference(@PathVariable(name="class_id")String classId)
            throws OpenViduJavaClientException, OpenViduHttpException {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, conferenceRepository.findByClassId(classId)));
    }

    // 회의 수정 (제목, 내용, updated_at)
    @PatchMapping("/{class_id}")
    public ResponseEntity<? extends BaseResponseBody>modifyConference(@PathVariable(name="class_id")String classId,
                                                                      @RequestBody(required = false) Map<String, Object> info)
            throws OpenViduJavaClientException, OpenViduHttpException {
        String title = (String)info.get("title");
        String description = (String)info.get("description");

        Conference conference = conferenceRepository.findByClassId(classId);
        conference.setTitle(title);
        conference.setDescription(description);

        conferenceRepository.save(conference);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, conference));
    }


}
