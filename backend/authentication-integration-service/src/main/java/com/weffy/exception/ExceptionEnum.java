package com.weffy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {

    USER_EXIST(HttpStatus.BAD_REQUEST,4000,"회원이 존재합니다."),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST,4001,"회원이 존재하지 않습니다."),
    USER_WITHDRAW(HttpStatus.BAD_REQUEST,4002,"탈퇴한 회원입니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST,4003,"올바르지 않은 사용자의 접근입니다."),
    MATTERMOST_LOGIN_FAILED(HttpStatus.BAD_REQUEST,4004,"mattermost 로그인 실패"),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST,4005,"mattermost 이미지가 존재하지 않습니다."),
    FILE_NOT_FOUND(HttpStatus.BAD_REQUEST,4006,"파일이 존재하지 않습니다."),
    CANNOT_UPLOAD_FILE(HttpStatus.BAD_REQUEST,4007,"파일 업로드에 실패하였습니다."),
    CANNOT_DOWNLOAD_FILE(HttpStatus.BAD_REQUEST,4008,"파일 다운로드 실패하였습니다."),
    HEADER_MODIFICATION_FAILED(HttpStatus.BAD_REQUEST,4009,"헤더 변경을 실패하였습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.BAD_REQUEST,4010,"채널이 존재하지 않습니다."),
    CANNOT_CREATE_ROOM(HttpStatus.UNAUTHORIZED,4011,"해당 채널에서 weffy를 생성할 권한이 없습니다."),;


    private HttpStatus status;
    private int code;
    private String description;

    private ExceptionEnum(HttpStatus status, int code, String description){
        this.code=code;
        this.status=status;
        this.description=description;
    }
}