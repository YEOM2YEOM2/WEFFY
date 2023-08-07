package io.openvidu.basic.java.conference.service;

import io.openvidu.basic.java.conference.dto.request.ConferenceDto;

public interface ConferenceService {
    // 세션 생성하기
    void createSession(ConferenceDto dto);
}
