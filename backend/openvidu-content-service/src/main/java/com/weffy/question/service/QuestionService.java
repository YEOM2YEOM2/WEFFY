package com.weffy.question.service;

import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;
import com.weffy.question.dto.response.QuestionStateResDto;
import com.weffy.question.entity.Question;

import java.util.List;

public interface QuestionService {
    QuestionResDto createQuestion(QuestionReqDto questionReqDto);

    List<QuestionStateResDto> getQuestions(String conferenceId);

    void completeQuestion(Long questionId);
}
