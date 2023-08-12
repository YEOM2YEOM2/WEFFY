package com.weffy.quiz.service;

import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.quiz.repository.JpaAnswerRepository;
import com.weffy.quiz.dto.request.AnswerReqDto;
import com.weffy.quiz.dto.request.QuizReqDto;
import com.weffy.quiz.dto.response.AnswerResDto;
import com.weffy.quiz.dto.response.QuizResDto;
import com.weffy.quiz.entity.Answer;
import com.weffy.quiz.entity.ChoiceOption;
import com.weffy.quiz.entity.Quiz;
import com.weffy.quiz.repository.JpaQuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service("QuizService")
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final JpaQuizRepository jpaQuizRepository;
    private final JpaAnswerRepository jpaAnswerRepository;

    @Override
    @Transactional
    public QuizResDto createQuiz(QuizReqDto quizReqDto) {
        Quiz quiz = Quiz.builder()
                .senderId(quizReqDto.getSenderId())
                .conferenceId(quizReqDto.getConferenceId())
                .content(quizReqDto.getContent())
                .build();

        if (quizReqDto.getOptions() != null) {
            for (String content : quizReqDto.getOptions()) {
                ChoiceOption option = new ChoiceOption(quiz, content);
                quiz.getOptions().add(option);
            }
        }

        jpaQuizRepository.save(quiz);

        new QuizResDto();
        return QuizResDto.of(quiz);
    }

    @Override
    public List<QuizResDto> getQuizzes(String conferenceId) {
        List<Quiz> quizzes = jpaQuizRepository.findByConferenceId(conferenceId);
        return quizzes.stream()
                .map(QuizResDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public QuizResDto getQuiz(Long quizId) {
        Quiz quiz = findById(quizId);
        new QuizResDto();
        return QuizResDto.of(quiz);
    }

    private Quiz findById(Long quizId) {
        return jpaQuizRepository.findById(quizId)
                .orElseThrow(() -> new CustomException(ExceptionEnum.QUIZ_NOT_FOUND));
    }

    @Override
    @Transactional
    public AnswerResDto createAnswer(Long quizId, AnswerReqDto answerReqDto) {
        Quiz quiz = findById(quizId);
        Answer answer = Answer.builder()
                .senderId(answerReqDto.getSenderId())
                .content(answerReqDto.getContent())
                .quiz(quiz)
                .build();

        jpaAnswerRepository.save(answer);
        new AnswerResDto();
        return AnswerResDto.of(answer);
    }
}
