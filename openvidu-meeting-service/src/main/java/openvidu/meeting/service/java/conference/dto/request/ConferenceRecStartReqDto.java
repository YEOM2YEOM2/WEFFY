package openvidu.meeting.service.java.conference.dto.request;

import io.openvidu.java.client.Recording;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConferenceRecStartReqDto {
    private String classId;
    private Recording.OutputMode outputMode;
    private boolean hasAudio;
    private boolean hasVideo;

    @Builder
    public ConferenceRecStartReqDto(String classId, Recording.OutputMode outputMode, boolean hasAudio, boolean hasVideo) {
        this.classId = classId;
        this.outputMode = outputMode;
        this.hasAudio = hasAudio;
        this.hasVideo = hasVideo;
    }
}
