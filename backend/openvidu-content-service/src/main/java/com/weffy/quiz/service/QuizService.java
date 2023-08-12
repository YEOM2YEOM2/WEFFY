package com.weffy.quiz.service;

import com.weffy.quiz.dto.request.QuizReqDto;
import com.weffy.quiz.dto.response.QuizResDto;

public interface QuizService {
    QuizResDto createQuiz(QuizReqDto quizReqDto);
}
