package com.weffy.quiz.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizReqDto {
    String senderId;
    String conferenceId;
    String content;
    List<String> options;
}
