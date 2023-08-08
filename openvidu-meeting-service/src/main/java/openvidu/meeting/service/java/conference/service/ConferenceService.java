package openvidu.meeting.service.java.conference.service;

import openvidu.meeting.service.java.conference.dto.request.ConferenceDto;

public interface ConferenceService {
    // 세션 생성하기
    void createSession(ConferenceDto dto);
}
