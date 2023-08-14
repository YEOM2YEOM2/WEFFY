package com.weffy.question.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId;
    private String conferenceId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private boolean isComplete;

    private boolean isAnonymous;

    @CreatedDate
    private LocalDateTime sendAt;

    @Builder
    public Question(String senderId, String conferenceId, String content, boolean isComplete, boolean isAnonymous) {
        this.senderId = senderId;
        this.conferenceId = conferenceId;
        this.content = content;
        this.isComplete = isComplete;
        this.isAnonymous = isAnonymous;
    }
}
