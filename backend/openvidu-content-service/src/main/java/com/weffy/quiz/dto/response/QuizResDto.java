package com.weffy.quiz.dto.response;

import com.weffy.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizResDto {
    Long id;
    String senderId;
    String conferenceId;
    String content;
    Long parent;

    public QuizResDto(Quiz quiz) {
        this.id = quiz.getId();
        this.senderId = quiz.getSenderId();
        this.conferenceId = quiz.getConferenceId();
        this.content = quiz.getContent();
        this.parent = quiz.getParent();
    }
}
