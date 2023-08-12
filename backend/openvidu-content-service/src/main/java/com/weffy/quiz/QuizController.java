package com.weffy.quiz;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.quiz.dto.request.AnswerReqDto;
import com.weffy.quiz.dto.request.QuizReqDto;
import com.weffy.quiz.dto.response.AnswerResDto;
import com.weffy.quiz.dto.response.QnAResDto;
import com.weffy.quiz.dto.response.QuizResDto;
import com.weffy.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "2. Quiz API", description = "weffy quiz api")
public class QuizController {
    private final QuizService quizService;


    // quiz 질문 생성
    @Operation(summary = "Quiz 생성", description = "option은 객관식 용(생략 가능) \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "CREATED", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuizResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 201,\n" +
                            "    \"data\": {\n" +
                            "        \"id\": 1,\n" +
                            "        \"senderId\": \"senderId\",\n" +
                            "        \"content\": \"content\",\n" +
                            "        \"sendAt\": \"0000-00-00T00:00:00.0000000\",\n" +
                            "        \"options\": [\n" +
                            "            {\n" +
                            "                \"id\": 1,\n" +
                            "                \"content\": \"option1\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"id\": 2,\n" +
                            "                \"content\": \"option2\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"id\": 3,\n" +
                            "                \"content\": \"option3\"\n" +
                            "            }\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4003", description =  "QUESTION_CREATION_FAILURE", content = @Content(examples = @ExampleObject(value = "{\"status\": 4003, \"data\": \"질문 생성에 실패하였습니다.\"}"))),
    })
    @PostMapping("/quiz")
    public ResponseEntity<? extends BaseResponseBody> createQuiz(@RequestBody QuizReqDto quizReqDto) {
        try {
            QuizResDto quizResDto = quizService.createQuiz(quizReqDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, quizResDto));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.QUIZ_CREATION_FAILURE);
        }
    }

    // conference의 모든 quiz 질문 리스트 조회
    @Operation(summary = "conference의 모든 quiz 질문 리스트 조회" )
    @Parameter(name = "conference_id", description = "회의 방 session id", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuizResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": [\n" +
                            "        {\n" +
                            "            \"id\": 1,\n" +
                            "            \"senderId\": \"senderId\",\n" +
                            "            \"content\": \"content\",\n" +
                            "            \"sendAt\": \"0000-00-00T00:00:00.0000000\",\n" +
                            "            \"options\": [\n" +
                            "                {\n" +
                            "                    \"id\": 1,\n" +
                            "                    \"content\": \"option1\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 2,\n" +
                            "                    \"content\": \"option2\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 3,\n" +
                            "                    \"content\": \"option3\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 3,\n" +
                            "            \"senderId\": \"senderId1\",\n" +
                            "            \"content\": \"content1\",\n" +
                            "            \"sendAt\": \"0000-00-00T00:00:00.0000000\",\n" +
                            "            \"options\": [\n" +
                            "                {\n" +
                            "                    \"id\": 7,\n" +
                            "                    \"content\": \"option1\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 8,\n" +
                            "                    \"content\": \"option2\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 9,\n" +
                            "                    \"content\": \"option3\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}"))),
            @ApiResponse(responseCode = "4004", description =  "QUESTION_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4004, \"data\": \"질문이 존재하지 않습니다.\"}"))),
    })
    @GetMapping("/conference/{conference_id}/quizzes")
    public ResponseEntity<? extends BaseResponseBody> getQuestionList(@PathVariable(name = "conference_id") String conferenceId) {
        List<QuizResDto> quizzes= quizService.getQuizzes(conferenceId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, quizzes));
    }

    // quiz 질문 조회
    @Operation(summary = "Quiz 질문 조회" )
    @Parameter(name = "quiz_id", description = "quiz의 id", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuizResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"id\": 1,\n" +
                            "        \"senderId\": \"senderId\",\n" +
                            "        \"content\": \"content\",\n" +
                            "        \"sendAt\": \"0000-00-00T00:00:00.0000000\",\n" +
                            "        \"options\": [\n" +
                            "            {\n" +
                            "                \"id\": 1,\n" +
                            "                \"content\": \"option1\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"id\": 2,\n" +
                            "                \"content\": \"option2\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"id\": 3,\n" +
                            "                \"content\": \"option3\"\n" +
                            "            }\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4004", description =  "QUESTION_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4004, \"data\": \"질문이 존재하지 않습니다.\"}"))),
    })
    @GetMapping("/quiz/{quiz_id}")
    public ResponseEntity<? extends BaseResponseBody> getQuestion(@PathVariable(name = "quiz_id") Long quizId) {
        QuizResDto quiz= quizService.getQuiz(quizId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, quiz));
    }

    // Answer 생성
    @Operation(summary = "Answer 생성" )
    @Parameter(name = "quiz_id", description = "quiz의 id", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "CREATED", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnswerResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 201,\n" +
                            "    \"data\": {\n" +
                            "        \"id\": 1,\n" +
                            "        \"senderId\": \"senderId\",\n" +
                            "        \"content\": \"content\",\n" +
                            "        \"sendAt\": \"0000-00-00T00:00:00.0000000\"\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4003", description =  "QUESTION_CREATION_FAILURE", content = @Content(examples = @ExampleObject(value = "{\"status\": 4003, \"data\": \"질문 생성에 실패하였습니다.\"}"))),
    })
    @PostMapping("/quiz/{quiz_id}/answer")
    public ResponseEntity<? extends BaseResponseBody> createQuiz(@PathVariable(name = "quiz_id") Long quizId, @RequestBody AnswerReqDto answerReqDto) {
        try {
            AnswerResDto answerResDto = quizService.createAnswer(quizId, answerReqDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, answerResDto));
        } catch (Exception e) {
            throw new CustomException(ExceptionEnum.ANSWER_CREATION_FAILURE);
        }
    }

    // quiz의 모든 답변 리스트 조회
    @Operation(summary = "quiz의 모든 답변 리스트 조회" )
    @Parameter(name = "quiz_id", description = "quiz의 id", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QnAResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"id\": 1,\n" +
                            "        \"senderId\": \"senderId\",\n" +
                            "        \"content\": \"content\",\n" +
                            "        \"sendAt\": \"0000-00-00T00:00:00.0000000\",\n" +
                            "        \"options\": [\n" +
                            "            {\n" +
                            "                \"id\": 1,\n" +
                            "                \"content\": \"option1\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"id\": 2,\n" +
                            "                \"content\": \"option2\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"id\": 3,\n" +
                            "                \"content\": \"option3\"\n" +
                            "            }\n" +
                            "        ],\n" +
                            "        \"answers\": [\n" +
                            "            {\n" +
                            "                \"id\": 1,\n" +
                            "                \"senderId\": \"senderId\",\n" +
                            "                \"content\": \"content\",\n" +
                            "                \"sendAt\": \"0000-00-00T00:00:00.0000000\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"id\": 2,\n" +
                            "                \"senderId\": \"senderId\",\n" +
                            "                \"content\": \"content\",\n" +
                            "                \"sendAt\": \"0000-00-00T00:00:00.0000000\"\n" +
                            "            }\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4003", description =  "QUESTION_CREATION_FAILURE", content = @Content(examples = @ExampleObject(value = "{\"status\": 4003, \"data\": \"질문 생성에 실패하였습니다.\"}"))),
    })
    @GetMapping("/quiz/{quiz_id}/answers")
    public ResponseEntity<? extends BaseResponseBody> getQuizAnswerList(@PathVariable(name = "quiz_id") Long quizId) {
        QnAResDto qnAResDto= quizService.getQuizAnswers(quizId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, qnAResDto));
    }
}
