package openvidu.meeting.service.java.conference.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConferenceDto {
    private String identification;
    private String classId;
    private String title;
    private String description;
    private boolean active;

    @Builder
    public ConferenceDto(String identification, String classId, String title, String description, boolean active) {
        this.identification = identification;
        this.classId = classId;
        this.title = title;
        this.description = description;
        this.active = active;
    }
}
