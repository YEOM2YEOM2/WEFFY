package com.weffy.quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId;
    private String conferenceId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoiceOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @CreatedDate
    private LocalDateTime sendAt;

    @Builder
    public Quiz(String senderId, String conferenceId, String content, List<ChoiceOption> options, List<Answer> answers) {
        this.senderId = senderId;
        this.conferenceId = conferenceId;
        this.content = content;
        this.options = (options != null) ? options : new ArrayList<>();
        this.answers = (answers != null) ? answers : new ArrayList<>();
    }
}
