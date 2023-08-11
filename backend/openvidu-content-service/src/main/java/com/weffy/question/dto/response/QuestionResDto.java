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

    public QuestionResDto of(Question question) {
        QuestionResDto res = new QuestionResDto();
        res.id = question.getId();
        res.senderId = question.getSenderId();
        res.conferenceId = question.getConferenceId();
        res.content = question.getContent();
        res.isComplete = question.isComplete();
        res.isAnonymous = question.isAnonymous();
        return res;
    }
}
