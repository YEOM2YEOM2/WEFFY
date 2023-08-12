package com.weffy.quiz.service;

import com.weffy.quiz.dto.request.QuizReqDto;
import com.weffy.quiz.dto.response.QuizResDto;
import com.weffy.quiz.entity.ChoiceOption;
import com.weffy.quiz.entity.Quiz;
import com.weffy.quiz.repository.JpaQuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service("QuizService")
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final JpaQuizRepository jpaQuizRepository;

    @Override
    @Transactional
    public QuizResDto createQuiz(QuizReqDto quizReqDto) {
        Quiz quiz = Quiz.builder()
                .senderId(quizReqDto.getSenderId())
                .conferenceId(quizReqDto.getConferenceId())
                .content(quizReqDto.getContent())
                .build();

        for (String content: quizReqDto.getOptions()) {
            ChoiceOption option = new ChoiceOption(quiz, content);
            quiz.getOptions().add(option);
        }

        jpaQuizRepository.save(quiz);

        new QuizResDto();
        return QuizResDto.of(quiz);
    }
}
