package com.weffy.question;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;
import com.weffy.question.dto.response.QuestionStateResDto;
import com.weffy.question.entity.Question;
import com.weffy.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("")
    public ResponseEntity<? extends BaseResponseBody> createQuestion(@RequestBody QuestionReqDto questionReqDto) {
        try {
            QuestionResDto questionResDto = questionService.createQuestion(questionReqDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, questionResDto));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.QUESTION_NOT_FOUND);
        }
    }

    @GetMapping("/{conference_id}")
    public ResponseEntity<? extends BaseResponseBody> getQuestion(@PathVariable(name = "conference_id") String conferenceId) {
        List<QuestionStateResDto> questions = questionService.getQuestions(conferenceId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, questions));
    }

    @PatchMapping("/{question_id}")
    public ResponseEntity<? extends BaseResponseBody> completeQuestion(@PathVariable(name = "question_id") Long questionId) {
        try {
            questionService.completeQuestion(questionId);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "SUCCESS"));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.UPDATE_COMPLETION_FAILED);
        }
    }

}
