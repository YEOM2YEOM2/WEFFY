package com.weffy.user.dto.Response;

import com.weffy.token.dto.response.CreateTokenResDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResDto {
    String identification;
    String accessToken;
    String refreshToken;
    String csrfToken;

    public UserSignInResDto of(String identification, CreateTokenResDto createTokenResDto) {
        UserSignInResDto res = new UserSignInResDto();
        res.identification = identification;
        res.accessToken = createTokenResDto.getAccessToken();
        res.refreshToken = createTokenResDto.getRefreshToken();
        res.csrfToken = createTokenResDto.getCsrfToken();
        return res;
    }
}