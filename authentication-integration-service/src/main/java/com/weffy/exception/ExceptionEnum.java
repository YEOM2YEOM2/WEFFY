package com.weffy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {

    USEREXIST(HttpStatus.BAD_REQUEST,4000,"회원이 존재합니다."),
    USERNOTEXIST(HttpStatus.BAD_REQUEST,4001,"회원이 존재하지 않습니다."),
    USERWITHDRAW(HttpStatus.BAD_REQUEST,4002,"탈퇴한 회원입니다."),
    INVALIDUSER(HttpStatus.BAD_REQUEST,4003,"올바르지 않은 사용자의 접근입니다."),
    MATTERMOSTLOGINFAILED(HttpStatus.BAD_REQUEST,4004,"mattermost 로그인 실패"),
    IMAGENOTFOUND(HttpStatus.BAD_REQUEST,4005,"mattermost 이미지가 존재하지 않습니다."),
    FILENOTFOUND(HttpStatus.BAD_REQUEST,4006,"파일이 존재하지 않습니다."),;


    private HttpStatus status;
    private int code;
    private String description;

    private ExceptionEnum(HttpStatus status, int code, String description){
        this.code=code;
        this.status=status;
        this.description=description;
    }
}