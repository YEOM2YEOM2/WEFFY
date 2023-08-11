package com.weffy.question.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionReqDto {
    String senderId;
    String conferenceId;
    String content;
    boolean isComplete;
    boolean isAnonymous;
}
