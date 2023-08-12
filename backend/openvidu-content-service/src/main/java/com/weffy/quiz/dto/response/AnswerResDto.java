package com.weffy.quiz.dto.response;

import com.weffy.quiz.entity.Answer;
import com.weffy.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerResDto {
    Long id;
    String senderId;
    String content;
    LocalDateTime sendAt;

    public static AnswerResDto of(Answer answer) {
        AnswerResDto dto = new AnswerResDto();
        dto.id = answer.getId();
        dto.senderId = answer.getSenderId();
        dto.content = answer.getContent();
        dto.sendAt = answer.getSendAt();
        return dto;
    }
}
