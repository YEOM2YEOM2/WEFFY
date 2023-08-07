package com.weffy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {




    private HttpStatus status;
    private int code;
    private String description;

    private ExceptionEnum(HttpStatus status, int code, String description){
        this.code=code;
        this.status=status;
        this.description=description;
    }
}