package io.openvidu.basic.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import io.openvidu.basic.java.ov.ServerDB;
import io.openvidu.java.client.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
	Logger logger = LoggerFactory.getLogger("io.openvidu.basic.java.Controller");

	@Value("${OPENVIDU_URL}")
	private String OPENVIDU_URL;

	@Value("${OPENVIDU_SECRET}")
	private String OPENVIDU_SECRET;

	private OpenVidu openvidu; // 오픈비듀 서버 생성

	// Collection to pair session names and OpenVidu Session objects
	//private Map<String, Session> mapSessions;

	// Collection to pair session names and tokens (the inner Map pairs tokens and
	// role associated)
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens; // <sessionId, <token, role>>

	// Collection to pair session names and recording objects
	private Map<String, Boolean> sessionRecordings;

	@PostConstruct
	public void init() {
		openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);

		//mapSessions = new ConcurrentHashMap<>();
		mapSessionNamesTokens = new ConcurrentHashMap<>();
		sessionRecordings = new ConcurrentHashMap<>();
	}


	/**
	 * @param params The Session properties
	 * @return The Session ID
	 */
	@PostMapping("/api/sessions")
	public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
			throws OpenViduJavaClientException, OpenViduHttpException {

		logger.debug("====================initializeSession====================");

		SessionProperties properties = SessionProperties.fromJson(params).build(); // customSessionId : "sessionId"

		// 해당 세션이 이미 만들어진 경우
		if(openvidu.getActiveSession(properties.customSessionId()) != null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Session session = openvidu.createSession(properties);

		// 토큰 관리 저장소 생성
		mapSessionNamesTokens.put(session.getSessionId(), new HashMap<String, OpenViduRole>());

		return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
	}


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












}
