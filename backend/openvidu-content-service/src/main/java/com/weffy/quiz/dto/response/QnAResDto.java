package com.weffy.quiz.dto.response;

import com.weffy.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QnAResDto {
    Long id;
    String senderId;
    String content;
    List<AnswerResDto> answers;

    public QnAResDto(Quiz quiz, List<AnswerResDto> answers) {
        this.id = quiz.getId();
        this.senderId = quiz.getSenderId();
        this.content = quiz.getContent();
        this.answers = answers;
    }
}
