package com.weffy.question.service;

import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.question.Repository.JpaQuestionRepository;
import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;
import com.weffy.question.entity.Question;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.desktop.QuitResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Transactional
@Service("QuestionService")
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{
    private final JpaQuestionRepository jpaQuestionRepository;

    @Override
    public QuestionResDto createQuestion(QuestionReqDto questionReqDto) {
        Question question = Question.builder()
                .senderId(questionReqDto.getSenderId())
                .conferenceId(questionReqDto.getConferenceId())
                .content(questionReqDto.getContent())
                .isComplete(questionReqDto.isComplete())
                .isAnonymous(questionReqDto.isAnonymous())
                .build();
        jpaQuestionRepository.save(question);
        return new QuestionResDto().of(question);
    }

}
