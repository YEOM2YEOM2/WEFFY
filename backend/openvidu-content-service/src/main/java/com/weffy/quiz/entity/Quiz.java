package com.weffy.quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId;
    private String conferenceId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private Long parent;

    @CreatedDate
    private LocalDateTime sendAt;

    @Builder
    public Quiz(String senderId, String conferenceId, String content, Long parent, LocalDateTime sendAt) {
        this.senderId = senderId;
        this.conferenceId = conferenceId;
        this.content = content;
        this.parent = parent;
        this.sendAt = sendAt;
    }
}
