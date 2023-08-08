package openvidu.meeting.service.java.conference.service;

import openvidu.meeting.service.java.conference.dto.request.ConferenceDto;
import openvidu.meeting.service.java.conference.entity.Conference;
import openvidu.meeting.service.java.conference.repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("conferenceService")
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService{

    private final ConferenceRepository conferenceRepository;

    @Override
    public void createSession(ConferenceDto dto) {
        conferenceRepository.save(
                Conference.builder()
                        .identification(dto.getIdentification())
                        .conference_url(dto.getConference_url())
                        .classId(dto.getClassId())
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .active(dto.isActive())
                        .build()
        );
    }
}
