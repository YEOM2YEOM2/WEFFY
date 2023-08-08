package openvidu.meeting.service.java.conference.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ConferenceCreateReqDto {
    private String identification;
    private String classId;
    private String title;
    private String description;
    private boolean active;

    @Builder
    public ConferenceCreateReqDto(String identification, String classId, String title, String description, boolean active) {
        this.identification = identification;
        this.classId = classId;
        this.title = title;
        this.description = description;
        this.active = active;
    }
}
