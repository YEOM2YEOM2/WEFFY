package openvidu.meeting.service.java.conference.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConferenceDetailResDto {
    private String title;
    private String description;
    private Date updatedAt;

    @Builder
    public ConferenceDetailResDto(String title, String description, Date updatedAt) {
        this.title = title;
        this.description = description;
        this.updatedAt = updatedAt;
    }
}
