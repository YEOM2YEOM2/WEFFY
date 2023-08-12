package com.weffy.quiz.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizReqDto {
    String senderId;
    String conferenceId;
    String content;
}
