package openvidu.meeting.service.java.conference.service;

import openvidu.meeting.service.java.conference.dto.response.ConferenceCreateResDto;
import openvidu.meeting.service.java.conference.entity.Conference;
import openvidu.meeting.service.java.conference.repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service("conferenceService")
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService{

    private final ConferenceRepository conferenceRepository;

    @Override
    public Conference createSession(ConferenceCreateResDto dto) {
        return conferenceRepository.save(
                Conference.builder()
                        .identification(dto.getIdentification())
                        .conferenceUrl(dto.getConferenceUrl())
                        .classId(dto.getClassId())
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .active(dto.isActive())
                        .build()
        );
    }

    @Override
    public Page<Conference> recentConference(String identification) {
        Pageable pageable = PageRequest.of(0,6);
        return conferenceRepository.findByIdentificationOrderByUpdatedAtDesc(identification, pageable);
    }


}
