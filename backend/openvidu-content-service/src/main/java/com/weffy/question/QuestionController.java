package com.weffy.question;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;
import com.weffy.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
