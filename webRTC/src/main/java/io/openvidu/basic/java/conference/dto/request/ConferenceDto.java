package io.openvidu.basic.java.conference.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConferenceDto {
    private String ownerId;
    private String conference_url;
    private String classId;
    private String title;
    private String description;
    private boolean active;

    @Builder
    public ConferenceDto(String ownerId, String conference_url, String classId, String title, String description, boolean active) {
        this.ownerId = ownerId;
        this.conference_url = conference_url;
        this.classId = classId;
        this.title = title;
        this.description = description;
        this.active = active;
    }
}
