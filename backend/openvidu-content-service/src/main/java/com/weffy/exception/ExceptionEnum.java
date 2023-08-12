package com.weffy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {

    QUESTION_CREATION_FAILURE(HttpStatus.BAD_REQUEST,4000,"질문 생성에 실패하였습니다."),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND,4001,"질문이 존재하지 않습니다."),
    UPDATE_COMPLETION_FAILED(HttpStatus.BAD_REQUEST,4002,"질문 답변 실패"),
    QUIZ_CREATION_FAILURE(HttpStatus.BAD_REQUEST,4003,"질문 생성에 실패하였습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND,4004,"질문이 존재하지 않습니다."),
   ;


    private HttpStatus status;
    private int code;
    private String description;

    private ExceptionEnum(HttpStatus status, int code, String description){
        this.code=code;
        this.status=status;
        this.description=description;
    }
}