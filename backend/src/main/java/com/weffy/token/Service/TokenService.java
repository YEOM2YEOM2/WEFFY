package com.weffy.token.Service;

import com.weffy.token.Dto.Response.CreateAccessTokenResDto;

public interface TokenService {
    CreateAccessTokenResDto createNewAccessToken(String refreshToken);
}
