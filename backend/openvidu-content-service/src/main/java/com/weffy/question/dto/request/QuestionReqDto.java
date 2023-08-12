package com.weffy.question.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionReqDto {
    String senderId;
    String conferenceId;
    String content;
    boolean isAnonymous;
}
