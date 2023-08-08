package openvidu.meeting.service.java.history.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Conference_History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user_identification;

    private Long conference_id;

    @Enumerated(value = EnumType.STRING)
    private Active active;

    private Date insert_time;

    @Builder
    public Conference_History(Long id, String user_identification, Long conference_id, Active active, Date insert_time) {
        this.id = id;
        this.user_identification = user_identification;
        this.conference_id = conference_id;
        this.active = active;
        this.insert_time = insert_time;
    }
}
