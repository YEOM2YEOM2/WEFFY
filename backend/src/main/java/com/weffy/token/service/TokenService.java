package com.weffy.token.service;

import com.weffy.token.dto.response.CreateAccessTokenResDto;

public interface TokenService {
    CreateAccessTokenResDto createNewAccessToken(String refreshToken);
}
