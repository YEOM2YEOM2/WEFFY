package com.weffy.quiz.service;

import com.weffy.quiz.dto.request.QuizReqDto;
import com.weffy.quiz.dto.response.QuizResDto;

import java.util.List;

public interface QuizService {
    QuizResDto createQuiz(QuizReqDto quizReqDto);

    List<QuizResDto> getQuiz(String conferenceId);
}
