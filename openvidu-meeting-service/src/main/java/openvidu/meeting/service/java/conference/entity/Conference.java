package openvidu.meeting.service.java.conference.entity;


import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "conference")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Conference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="identification")
    private String identification;

    @NotNull
    private String conference_url;

    @NotNull
    @Column(name = "class_id")
    private String classId;

    private String title;

    private String description;

    private boolean active;

    private Long parent;

    @CreationTimestamp
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;

    @PreUpdate
    protected void onUpdate() {
        updated_at = new Date();
    }

    @Builder
    public Conference(Long id, String identification, String conference_url, String classId, String title,
                      String description, boolean active, Long parent, Date created_at, Date updated_at) {
        this.id = id;
        this.identification = identification;
        this.conference_url = conference_url;
        this.classId = classId;
        this.title = title;
        this.description = description;
        this.active = active;
        this.parent = parent;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @PrePersist
    public void prePersist() {
        this.parent = this.parent == null ? 0 : this.parent;
    }
}