package openvidu.meeting.service.java.conference.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ConferenceCreateResDto {
    private String identification;
    private String conferenceUrl;
    private String classId;
    private String title;
    private String description;
    private boolean active;

    @Builder
    public ConferenceCreateResDto(String identification, String conferenceUrl, String classId, String title, String description, boolean active) {
        this.identification = identification;
        this.conferenceUrl = conferenceUrl;
        this.classId = classId;
        this.title = title;
        this.description = description;
        this.active = active;
    }
}
