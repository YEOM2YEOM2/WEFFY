package com.weffy.question.dto.response;

import com.weffy.question.entity.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResDto {
    Long id;
    String senderId;
    String conferenceId;
    String content;
    boolean isComplete;
    boolean isAnonymous;

}
