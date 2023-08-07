package com.weffy.token.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTokenResDto {
    String accessToken;
    String refreshToken;
    String csrfToken;

    public CreateTokenResDto of(String accessToken, String refreshToken, String csrfToken) {
        CreateTokenResDto res = new CreateTokenResDto();
        res.accessToken = accessToken;
        res.refreshToken = refreshToken;
        res.csrfToken = csrfToken;
        return res;
    }
}
