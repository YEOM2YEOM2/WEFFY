package io.openvidu.basic.java.conference.service;

import io.openvidu.basic.java.conference.dto.request.ConferenceDto;
import io.openvidu.basic.java.conference.entity.Conference;
import io.openvidu.basic.java.conference.repository.ConferenceRepository;
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
                        .ownerId(dto.getOwnerId())
                        .conference_url(dto.getConference_url())
                        .classId(dto.getClassId())
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .active(dto.isActive())
                        .build()
        );
    }
}
