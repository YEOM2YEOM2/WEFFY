package openvidu.meeting.service.java.conference.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConferenceDetailResDto {
    private String title;
  //  private String description;
    private boolean active;
    private Date updatedAt;

    @Builder
    public ConferenceDetailResDto(String title, boolean active, Date updatedAt) {
        this.title = title;
     //   this.description = description;
        this.active = active;
        this.updatedAt = updatedAt;
    }
}
