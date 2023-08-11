package com.weffy.question;

import com.weffy.common.dto.BaseResponseBody;
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
            QuestionResDto questionResDto = questionService.createQuestion(questionReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, questionResDto));
    }

    @GetMapping("/{conference_id}")
    public ResponseEntity<? extends BaseResponseBody> getQuestion(@PathVariable(name = "conference_id") String conferenceId) {
        List<QuestionStateResDto> questions = questionService.getQuestions(conferenceId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, questions));
    }

}
