package com.weffy.token.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTokenResDto {
    String accessToken;
    String refreshToken;

    public CreateTokenResDto of(String accessToken, String refreshToken) {
        CreateTokenResDto res = new CreateTokenResDto();
        res.accessToken = accessToken;
        res.refreshToken = refreshToken;
        return res;
    }
}
