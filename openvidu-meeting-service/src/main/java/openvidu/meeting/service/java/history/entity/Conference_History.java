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

    private String identification;

    @Column(name = "conference_id")
    private Long conferenceId;

    @Enumerated(value = EnumType.STRING)
    private Active active;

    private Date insert_time;

    @Builder
    public Conference_History(Long id, String identification, Long conference_id, Active active, Date insert_time) {
        this.id = id;
        this.identification = identification;
        this.conferenceId = conference_id;
        this.active = active;
        this.insert_time = insert_time;
    }
}
