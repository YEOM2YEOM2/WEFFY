package com.weffy.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseBody<T> {

    private int status;
    private T data;

    // getters and setters

    public static <T> BaseResponseBody<T> of(int status, T data) {
        BaseResponseBody<T> body = new BaseResponseBody<>();
        body.setStatus(status);
        body.setData(data);
        return body;
    }
}
