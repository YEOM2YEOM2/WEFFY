package com.weffy.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private int errorCode;
    private String description;

    @Builder
    public ExceptionResponse(int errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }
}