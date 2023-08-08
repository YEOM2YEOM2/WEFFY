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
    private String conferenceUrl;

    @NotNull
    @Column(name = "class_id")
    private String classId;

    private String title;

    private String description;

    private boolean active;

    private Long parent;

    @CreationTimestamp
    @Column(name="created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Builder
    public Conference(Long id, String identification, String conferenceUrl, String classId, String title,
                      String description, boolean active, Long parent, Date createdAt, Date updatedAt) {
        this.id = id;
        this.identification = identification;
        this.conferenceUrl = conferenceUrl;
        this.classId = classId;
        this.title = title;
        this.description = description;
        this.active = active;
        this.parent = parent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void prePersist() {
        this.parent = this.parent == null ? 0 : this.parent;
    }
}