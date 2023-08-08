package openvidu.meeting.service.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import io.openvidu.java.client.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.openvidu.java.client.Recording;
import io.openvidu.java.client.RecordingProperties;


@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class Controller {
	Logger logger = LoggerFactory.getLogger("openvidu.meeting.service.java.Controller");
	private OpenVidu openvidu;
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens; // <sessionId, <token, role>>
	private Map<String, Boolean> sessionRecordings;


	@PostConstruct
	public void init() {
		openvidu = OpenviduDB.getOpenvidu();

		mapSessionNamesTokens = OpenviduDB.getMapSessionNameTokens();
		sessionRecordings = OpenviduDB.getSessionRecordings();
	}

	// 방 생성하기
	@PostMapping("/api/sessions")
	public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
			throws OpenViduJavaClientException, OpenViduHttpException {

		logger.debug("====================initializeSession====================");

		SessionProperties properties = SessionProperties.fromJson(params).build(); // customSessionId : "sessionId"

		// 해당 세션이 이미 만들어진 경우
		if(openvidu.getActiveSession(properties.customSessionId()) != null){
			System.out.println(properties.customSessionId()+"===============>");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Session session = openvidu.createSession(properties);

		// 토큰 관리 저장소 생성
		mapSessionNamesTokens.put(session.getSessionId(), new HashMap<String, OpenViduRole>());

		return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
	}


	// 방(세션)을 생성한다.
//	@PostMapping("/api/sessions")
//	public ResponseEntity<String> createConference(@RequestBody(required = false) Map<String, Object> params)
//			throws OpenViduJavaClientException, OpenViduHttpException {
//		System.out.println("---------------------createConference------------------");
//		SessionProperties properties = SessionProperties.fromJson(params).build();
//
//		// 방이 이미 만들어진 경우
//		if(properties.customSessionId() != null){
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//
//		// 방을 생성함(세션을 생성한다)
//		Session session = openvidu.createSession(properties);
//
//		// 방에 들어온 사람(토큰)을 저장할 저장소
//		mapSessionNamesTokens.put(session.getSessionId(), new HashMap<String, OpenViduRole>());
//
//		return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
//	}


	/**
	 * @param sessionId The Session in which to create the Connection
	 * @param params    The Connection properties
	 * @return The Token associated to the Connection
	 */
	@PostMapping("/api/sessions/{sessionId}/{role}/connections")
	public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
												   @PathVariable("role") String role,
												   @RequestBody(required = false) Map<String, Object> params)
			throws OpenViduJavaClientException, OpenViduHttpException {

		log.warn("====================createConnection====================");

		Session session = openvidu.getActiveSession(sessionId);

		if (session == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
		Connection connection = session.createConnection(properties);

		// 토큰 관리 저장소에 데이터를 넣음(SessionId, Token, Role)
		mapSessionNamesTokens.get(sessionId).put(connection.getToken(), OpenViduRole.valueOf(role));

		return new ResponseEntity<>(connection.getToken(), HttpStatus.OK); // 토큰을 반환함
	}
	// 하나의 세션에 각자 다른 토큰을 가지고 있음


	/*******************/
	/** Recording API **/
	/*******************/


	@RequestMapping(value = "/recording-java/api/recording/start", method = RequestMethod.POST)
	public ResponseEntity<?> startRecording(@RequestBody Map<String, Object> params) {
		String sessionId = (String) params.get("session");
		Recording.OutputMode outputMode = Recording.OutputMode.valueOf((String) params.get("outputMode")); // COMPOSED,INDIVIDUAL,COMPOSED_QUICK_START 에서 선택
		boolean hasAudio = (boolean) params.get("hasAudio");
		boolean hasVideo = (boolean) params.get("hasVideo");

		RecordingProperties properties = new RecordingProperties.Builder().outputMode(outputMode).hasAudio(hasAudio)
				.hasVideo(hasVideo).build();

		System.out.println("Starting recording for session " + sessionId + " with properties {outputMode=" + outputMode
				+ ", hasAudio=" + hasAudio + ", hasVideo=" + hasVideo + "}");

		try {
			Recording recording = this.openvidu.startRecording(sessionId, properties);
			this.sessionRecordings.put(sessionId, true);
			System.out.println(recording.getId());
			return new ResponseEntity<>(recording, HttpStatus.OK);
		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/recording-java/api/recording/stop", method = RequestMethod.POST)
	public ResponseEntity<?> stopRecording(@RequestBody Map<String, Object> params) {
		String recordingId = (String) params.get("recording");

		System.out.println("Stoping recording | {recordingId}=" + recordingId);

		try {
			Recording recording = this.openvidu.stopRecording(recordingId);
			this.sessionRecordings.remove(recording.getSessionId());
			return new ResponseEntity<>(recording, HttpStatus.OK);
		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/recording-java/api/recording/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRecording(@RequestBody Map<String, Object> params) {
		String recordingId = (String) params.get("recording");

		System.out.println("Deleting recording | {recordingId}=" + recordingId);

		try {
			this.openvidu.deleteRecording(recordingId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/recording-java/api/recording/get/{recordingId}", method = RequestMethod.GET)
	public ResponseEntity<?> getRecording(@PathVariable(value = "recordingId") String recordingId) {

		System.out.println("Getting recording | {recordingId}=" + recordingId);

		try {
			Recording recording = this.openvidu.getRecording(recordingId);
			return new ResponseEntity<>(recording, HttpStatus.OK);
		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/recording-java/api/recording/list", method = RequestMethod.GET)
	public ResponseEntity<?> listRecordings() {

		System.out.println("Listing recordings");

		try {
			List<Recording> recordings = this.openvidu.listRecordings();

			return new ResponseEntity<>(recordings, HttpStatus.OK);
		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}


}
