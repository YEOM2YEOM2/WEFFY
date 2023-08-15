//package openvidu.meeting.service.java;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import javax.annotation.PostConstruct;
//
//import io.openvidu.java.client.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import openvidu.meeting.service.java.common.dto.BaseResponseBody;
//import openvidu.meeting.service.java.conference.entity.Conference;
//import openvidu.meeting.service.java.conference.entity.UserRole;
//import openvidu.meeting.service.java.conference.repository.ConferenceRepository;
//import openvidu.meeting.service.java.conference.service.ConferenceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import io.openvidu.java.client.Recording;
//import io.openvidu.java.client.RecordingProperties;
//
//
//@Slf4j
//@CrossOrigin(origins = "*")
//@RestController
//@RequiredArgsConstructor
//public class Controller {
//	private OpenVidu openvidu;
//	private String root = "http://localhost:8082/";
//	//  private Map<String, String> mapIdentificationTokens;
//	private Map<String, Map<String, UserRole>> mapSessionNamesTokens; // <sessionId, <token, role>>
//	//private Map<String, Boolean> sessionRecordings;
//
//	private final ConferenceRepository conferenceRepository;
//
//	private final ConferenceService conferenceService;
//
//	@PostConstruct
//	public void init() throws OpenViduJavaClientException, OpenViduHttpException {
//		openvidu = OpenviduDB.getOpenvidu();
//
//		mapSessionNamesTokens = OpenviduDB.getMapSessionNameTokens();
//		//sessionRecordings = OpenviduDB.getSessionRecordings();
//
//		conferenceSetting();
//	}
//
//	// DB에 있는 방(세션)을 모두 오픈비두에 넣어준다.
//	public void conferenceSetting() throws OpenViduJavaClientException, OpenViduHttpException {
//		List<Conference> roomList = conferenceRepository.findAll();
//
//		SessionProperties properties;
//		Session session;
//		for(Conference conference : roomList){
//			properties = new SessionProperties.Builder().customSessionId(conference.getClassId()).build();
//			session = openvidu.createSession(properties);
//
//			mapSessionNamesTokens.put(conference.getClassId(), new HashMap<String, UserRole>()); // 방의 이름, 유저 아이디, Role
//		}
//	}
//
//	// 방 생성하기
////	@PostMapping("/api/sessions")
////	public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
////			throws OpenViduJavaClientException, OpenViduHttpException {
////
////		SessionProperties properties = SessionProperties.fromJson(params).build(); // customSessionId : "sessionId"
////
////		// 해당 세션이 이미 만들어진 경우
////		if(openvidu.getActiveSession(properties.customSessionId()) != null){
////			System.out.println(properties.customSessionId()+"===============>");
////			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////		}
////
////		Session session = openvidu.createSession(properties);
////
////		// 토큰 관리 저장소 생성
////		mapSessionNamesTokens.put(session.getSessionId(), new HashMap<String, UserRole>());
////
////		return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
////	}
//
//
//
//
//	// 방(세션)을 생성한다.
////	@PostMapping("/api/sessions")
////	public ResponseEntity<String> createConference(@RequestBody(required = false) Map<String, Object> params)
////			throws OpenViduJavaClientException, OpenViduHttpException {
////		System.out.println("---------------------createConference------------------");
////		SessionProperties properties = SessionProperties.fromJson(params).build();
////
////		// 방이 이미 만들어진 경우
////		if(properties.customSessionId() != null){
////			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////		}
////
////		// 방을 생성함(세션을 생성한다)
////		Session session = openvidu.createSession(properties);
////
////		// 방에 들어온 사람(토큰)을 저장할 저장소
////		mapSessionNamesTokens.put(session.getSessionId(), new HashMap<String, OpenViduRole>());
////
////		return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
////	}
//
//
//	@PostMapping("/api/sessions/{class_id}/{identification}/{role}/connections")
//	public ResponseEntity<String> createConnection(@PathVariable("class_id") String classId,
//												   @PathVariable("identification") String identification, @PathVariable("role") String role,
//												   @RequestBody(required = false) Map<String, Object> params)
//			throws OpenViduJavaClientException, OpenViduHttpException {
//
//		System.out.println(classId+"/"+identification+"/"+role);
//
//		Session session = openvidu.getActiveSession(classId);
//
//		if(session == null){
//			return new ResponseEntity<>("방이 없음", HttpStatus.NOT_FOUND);
//		}
//
//		ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
//
//		Connection connection = session.createConnection(properties);
//
//		System.out.println("ConnectionId : "+ connection.getConnectionId()+"/"+connection.getToken());
//
//		List<Publisher> list = connection.getPublishers();
//
//		System.out.println("---------------");
//		for(Publisher publisher : list){
//			System.out.println("정답 : "+ publisher.getStreamId());
//		}
//		System.out.println("---------------");
//
//		// 어디 방에 들어간 사람인지 구분하기 위함
//		//mapSessionNamesTokens.get(classId).put(identification, UserRole.valueOf(role));
//
//		return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
//	}
//	// 하나의 세션에 각자 다른 토큰을 가지고 있음
//
//
//	/*******************/
//	/** Recording API **/
//	/*******************/
//
////
////	@RequestMapping(value = "/recording-java/api/recording/start", method = RequestMethod.POST)
////	public ResponseEntity<?> startRecording(@RequestBody Map<String, Object> params) {
////		String sessionId = (String) params.get("session");
////		Recording.OutputMode outputMode = Recording.OutputMode.valueOf((String) params.get("outputMode")); // COMPOSED,INDIVIDUAL,COMPOSED_QUICK_START 에서 선택
////		boolean hasAudio = (boolean) params.get("hasAudio");
////		boolean hasVideo = (boolean) params.get("hasVideo");
////
////		RecordingProperties properties = new RecordingProperties.Builder().outputMode(outputMode).hasAudio(hasAudio)
////				.hasVideo(hasVideo).build();
////
////		System.out.println("Starting recording for session " + sessionId + " with properties {outputMode=" + outputMode
////				+ ", hasAudio=" + hasAudio + ", hasVideo=" + hasVideo + "}");
////
////		try {
////			Recording recording = this.openvidu.startRecording(sessionId, properties);
////			this.sessionRecordings.put(sessionId, true);
////			System.out.println(recording.getId());
////			return new ResponseEntity<>(recording, HttpStatus.OK);
////		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
////			e.printStackTrace();
////			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
////		}
////	}
////
////	@RequestMapping(value = "/recording-java/api/recording/stop", method = RequestMethod.POST)
////	public ResponseEntity<?> stopRecording(@RequestBody Map<String, Object> params) {
////		String recordingId = (String) params.get("recording");
////
////		System.out.println("Stoping recording | {recordingId}=" + recordingId);
////
////		try {
////			Recording recording = this.openvidu.stopRecording(recordingId);
////			this.sessionRecordings.remove(recording.getSessionId());
////			return new ResponseEntity<>(recording, HttpStatus.OK);
////		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
////			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
////		}
////	}
////
//	@RequestMapping(value = "/recording-java/api/recording/delete", method = RequestMethod.DELETE)
//	public ResponseEntity<?> deleteRecording(@RequestBody Map<String, Object> params) {
//		String recordingId = (String) params.get("recording");
//
//		System.out.println("Deleting recording | {recordingId}=" + recordingId);
//
//		try {
//			this.openvidu.deleteRecording(recordingId);
//			return new ResponseEntity<>(HttpStatus.OK);
//		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
//
////	@RequestMapping(value = "/recording-java/api/recording/get/{recordingId}", method = RequestMethod.GET)
////	public ResponseEntity<?> getRecording(@PathVariable(value = "recordingId") String recordingId) {
////
////		System.out.println("Getting recording | {recordingId}=" + recordingId);
////
////		try {
////			Recording recording = this.openvidu.getRecording(recordingId);
////			return new ResponseEntity<>(recording, HttpStatus.OK);
////		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
////			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
////		}
////	}
////
//	@RequestMapping(value = "/recording-java/api/recording/list", method = RequestMethod.GET)
//	public ResponseEntity<?> listRecordings() {
//
//		System.out.println("Listing recordings");
//
//		try {
//			List<Recording> recordings = this.openvidu.listRecordings();
//
//			return new ResponseEntity<>(recordings, HttpStatus.OK);
//		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
//
//
//}
