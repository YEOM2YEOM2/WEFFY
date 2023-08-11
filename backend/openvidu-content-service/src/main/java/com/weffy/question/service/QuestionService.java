package com.weffy.question.service;

import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;

public interface QuestionService {
    QuestionResDto createQuestion(QuestionReqDto questionReqDto);

}
