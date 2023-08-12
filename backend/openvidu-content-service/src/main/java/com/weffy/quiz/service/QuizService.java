package com.weffy.quiz.service;

import com.weffy.quiz.dto.request.AnswerReqDto;
import com.weffy.quiz.dto.request.QuizReqDto;
import com.weffy.quiz.dto.response.AnswerResDto;
import com.weffy.quiz.dto.response.QnAResDto;
import com.weffy.quiz.dto.response.QuizResDto;

import java.util.List;

public interface QuizService {
    QuizResDto createQuiz(QuizReqDto quizReqDto);

    List<QuizResDto> getQuizzes(String conferenceId);

    QuizResDto getQuiz(Long quizId);

    AnswerResDto createAnswer(Long quizId, AnswerReqDto answerReqDto);

    QnAResDto getQuizAnswers(Long quizId);
}
