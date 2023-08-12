package com.weffy.question;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.question.dto.request.QuestionReqDto;
import com.weffy.question.dto.response.QuestionResDto;
import com.weffy.question.dto.response.QuestionStateResDto;
import com.weffy.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class QuestionController {
    private final QuestionService questionService;

    @Operation(summary = "Question 생성", description = "Question 생성 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "CREATED", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionResDto.class),
                    examples = @ExampleObject(value = "{\"status\": 201, \"data\": {\"id\": 1,\n" +
                            "        \"senderId\": \"senderId\",\n" +
                            "        \"conferenceId\": \"conferenceId\",\n" +
                            "        \"content\": \"content,\",\n" +
                            "        \"anonymous\": false,\n" +
                            "        \"complete\": false}}"))),
            @ApiResponse(responseCode = "4000", description =  "QUESTION_CREATION_FAILURE", content = @Content(examples = @ExampleObject(value = "{\"status\": 4000, \"data\": \"질문 생성에 실패하였습니다.\"}"))),
    })
    @PostMapping("/question")
    public ResponseEntity<? extends BaseResponseBody> createQuestion(@RequestBody QuestionReqDto questionReqDto) {
        try {
            QuestionResDto questionResDto = questionService.createQuestion(questionReqDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, questionResDto));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.QUESTION_CREATION_FAILURE);
        }
    }

    @Operation(summary = "Question List 조회", description = "하나의 conference의 모든 question 리스트 \n\n" )
    @Parameter(name = "conference_id", description = "회의 방 session id", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionStateResDto.class),
                    examples = @ExampleObject(value = "{\"status\": 200, \"data\": [\n" +
                            "        {\n" +
                            "            \"id\": 1,\n" +
                            "            \"senderId\": \"senderId\",\n" +
                            "            \"content\": \"content,\",\n" +
                            "            \"isComplete\": false,\n" +
                            "            \"isAnonymous\": false\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 2,\n" +
                            "            \"senderId\": \"senderId\",\n" +
                            "            \"content\": \"content,\",\n" +
                            "            \"isComplete\": false,\n" +
                            "            \"isAnonymous\": false\n" +
                            "        }]}"))),
            @ApiResponse(responseCode = "4001", description =  "QUESTION_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"질문이 존재하지 않습니다.\"}"))),
    })
    @GetMapping("/conference/{conference_id}/questions")
    public ResponseEntity<? extends BaseResponseBody> getQuestionList(@PathVariable(name = "conference_id") String conferenceId) {
        List<QuestionStateResDto> questions = questionService.getQuestions(conferenceId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, questions));
    }

    @Operation(summary = "Question 조회", description = "하나의 Question조회 \n\n" )
    @Parameter(name = "question_id", description = "질문의 id", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionStateResDto.class),
                    examples = @ExampleObject(value = "{\"status\": 200, \"data\": " +
                            "        {\n" +
                            "            \"id\": 1,\n" +
                            "            \"senderId\": \"senderId\",\n" +
                            "            \"content\": \"content,\",\n" +
                            "            \"isComplete\": false,\n" +
                            "            \"isAnonymous\": false\n" +
                            "        }}"))),
            @ApiResponse(responseCode = "4001", description =  "QUESTION_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"질문이 존재하지 않습니다.\"}"))),
    })
    @GetMapping("/question/{question_id}")
    public ResponseEntity<? extends BaseResponseBody> getQuestion(@PathVariable(name = "question_id") Long questionId) {
        try {
            QuestionStateResDto question = questionService.getQuestion(questionId);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, question));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.QUESTION_NOT_FOUND);
        }
    }

    @Operation(summary = "Question 답변 완료", description = "답변 여부 체크 \n\n 답변 완료 <-> 답변 미완료" )
    @Parameter(name = "question_id", description = "질문의 id", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionStateResDto.class),
                    examples = @ExampleObject(value = "{\"status\": 200, \"data\": \"SUCCESS\"}"))),
            @ApiResponse(responseCode = "4001", description =  "QUESTION_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"질문이 존재하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "4002", description =  "UPDATE_COMPLETION_FAILED", content = @Content(examples = @ExampleObject(value = "{\"status\": 4002, \"data\": \"질문 답변 실패\"}")))
    })
    @PatchMapping("/question/{question_id}")
    public ResponseEntity<? extends BaseResponseBody> completeQuestion(@PathVariable(name = "question_id") Long questionId) {
        try {
            questionService.completeQuestion(questionId);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "SUCCESS"));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.UPDATE_COMPLETION_FAILED);
        }
    }

}
