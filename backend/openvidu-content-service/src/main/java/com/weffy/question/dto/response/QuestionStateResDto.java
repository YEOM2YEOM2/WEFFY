package com.weffy.question.dto.response;

import com.weffy.question.entity.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionStateResDto {
    Long id;
    String senderId;
    String content;
    boolean isComplete;
    boolean isAnonymous;

    public static QuestionStateResDto of(Question question) {
        QuestionStateResDto res = new QuestionStateResDto();
        res.id = question.getId();
        res.senderId = question.getSenderId();
        res.content = question.getContent();
        res.isComplete = question.isComplete();
        res.isAnonymous = question.isAnonymous();
        return res;
    }
}
