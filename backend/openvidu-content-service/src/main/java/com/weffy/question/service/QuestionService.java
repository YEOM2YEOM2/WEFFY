package com.weffy.question.service;

import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;
import com.weffy.question.dto.response.QuestionStateResDto;

import java.util.List;

public interface QuestionService {
    QuestionResDto createQuestion(QuestionReqDto questionReqDto);

    List<QuestionStateResDto> getQuestions(String conferenceId);

    QuestionStateResDto getQuestion(Long questionId);

    void completeQuestion(Long questionId);
}
