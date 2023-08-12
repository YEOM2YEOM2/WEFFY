package com.weffy.quiz.dto.request;

import com.weffy.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerReqDto {
    String senderId;
    String content;
}
