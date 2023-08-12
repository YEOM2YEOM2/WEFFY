package com.weffy.quiz.dto.response;

import com.weffy.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResDto {
    Long id;
    String senderId;
    String content;

    public AnswerResDto(Quiz quiz) {
        this.id = quiz.getId();
        this.senderId = quiz.getSenderId();
        this.content = quiz.getContent();
    }
}
