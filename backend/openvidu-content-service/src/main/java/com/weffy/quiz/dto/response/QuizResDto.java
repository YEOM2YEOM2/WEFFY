package com.weffy.quiz.dto.response;

import com.weffy.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class QuizResDto {
    Long id;
    String senderId;
    String content;
    LocalDateTime sendAt;
    List<ChoiceOptionResDto> options;

    public static QuizResDto of(Quiz quiz) {
        QuizResDto dto = new QuizResDto();
        dto.id = quiz.getId();
        dto.senderId = quiz.getSenderId();
        dto.content = quiz.getContent();
        dto.sendAt = quiz.getSendAt();
        dto.options = quiz.getOptions().stream().map(ChoiceOptionResDto::of).collect(Collectors.toList());
        return dto;
    }
}
