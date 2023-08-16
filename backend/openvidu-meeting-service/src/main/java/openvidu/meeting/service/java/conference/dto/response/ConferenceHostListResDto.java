package openvidu.meeting.service.java.conference.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConferenceHostListResDto {
    private String conferenceUrl;
    private String title;


    @Builder
    public ConferenceHostListResDto(String conferenceUrl, String title) {
        this.conferenceUrl = conferenceUrl;
        this.title = title;
    }
}
