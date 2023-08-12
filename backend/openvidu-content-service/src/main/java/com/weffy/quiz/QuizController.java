package com.weffy.quiz;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.question.dto.response.QuestionStateResDto;
import com.weffy.quiz.dto.request.QuizReqDto;
import com.weffy.quiz.dto.response.QuizResDto;
import com.weffy.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class QuizController {
    private final QuizService quizService;


    // quiz 질문 생성
    @PostMapping("/quiz")
    public ResponseEntity<? extends BaseResponseBody> createQuiz(@RequestBody QuizReqDto quizReqDto) {
        try {
            QuizResDto quizResDto = quizService.createQuiz(quizReqDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, quizResDto));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.QUIZ_CREATION_FAILURE);
        }
    }

    // quiz 질문 조회
    @GetMapping("/conference/{conference_id}/quizzes")
    public ResponseEntity<? extends BaseResponseBody> getQuestionList(@PathVariable(name = "conference_id") String conferenceId) {
        List<QuizResDto> quizzes= quizService.getQuiz(conferenceId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, quizzes));
    }
}
