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
    Long id;

    String sender_id;
    String conference_id;

    @Column(columnDefinition = "LONGTEXT")
    String content;

    boolean isComplete;

    boolean isAnonymous;

    @CreatedDate
    private LocalDateTime sendAt;

    @Builder
    public Question(String sender_id, String conference_id, String content, boolean isComplete, boolean isAnonymous) {
        this.sender_id = sender_id;
        this.conference_id = conference_id;
        this.content = content;
        this.isComplete = isComplete;
        this.isAnonymous = isAnonymous;
    }
}
