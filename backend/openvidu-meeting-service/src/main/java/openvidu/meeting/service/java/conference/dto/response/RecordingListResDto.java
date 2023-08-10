package openvidu.meeting.service.java.conference.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordingListResDto {
    private String title;
    private String url;

    @Builder
    public RecordingListResDto(String title, String url) {
        this.title = title;
        this.url = url;
    }
}
