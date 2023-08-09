package openvidu.meeting.service.java.conference.service;

import openvidu.meeting.service.java.conference.dto.request.ConferenceCreateReqDto;
import openvidu.meeting.service.java.conference.dto.response.ConferenceCreateResDto;
import openvidu.meeting.service.java.conference.entity.Conference;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ConferenceService {
    // 세션 생성하기
    Conference createSession(ConferenceCreateResDto dto);

    Page<Conference> recentConference(String identification);
}
