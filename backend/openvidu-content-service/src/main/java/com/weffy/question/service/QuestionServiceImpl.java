package com.weffy.question.service;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;

import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;
import com.weffy.question.dto.response.QuestionStateResDto;
import com.weffy.question.entity.Question;
import com.weffy.question.repository.JpaQuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service("QuestionService")
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{
    private final JpaQuestionRepository jpaQuestionRepository;

    @Override
    @Transactional
    public QuestionResDto createQuestion(QuestionReqDto questionReqDto) {
        Question question = Question.builder()
                .senderId(questionReqDto.getSenderId())
                .conferenceId(questionReqDto.getConferenceId())
                .content(questionReqDto.getContent())
                .isComplete(false) // 처음 질문 생성 시 false default
                .isAnonymous(questionReqDto.isAnonymous())
                .build();
        jpaQuestionRepository.save(question);
        return new QuestionResDto().of(question);
    }

    @Override
    public List<QuestionStateResDto> getQuestions(String conferenceId) {
        List<Question> questions =  jpaQuestionRepository.findByConferenceId(conferenceId);
        return questions.stream()
                .map(QuestionStateResDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionStateResDto getQuestion(Long questionId) {
        Question question = findById(questionId);
        new QuestionStateResDto();
        return QuestionStateResDto.of(question);
    }

    @Override
    @Transactional
    public void completeQuestion(Long questionId) {
        Question question = findById(questionId);
        // 답변 미완료 <-> 답변 완료
        question.setComplete(!question.isComplete());
        jpaQuestionRepository.save(question);
    }

    private Question findById(Long questionId) {
        return jpaQuestionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ExceptionEnum.QUESTION_NOT_FOUND));
    }
}
