package com.weffy.token.service;

import com.weffy.token.dto.response.CreateAccessTokenResDto;
import com.weffy.token.config.TokenProvider;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service("TokenService")
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Override
    public CreateAccessTokenResDto createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getId();
        WeffyUser weffyUser = userService.findById(userId);

        String token =  tokenProvider.generateToken(weffyUser, Duration.ofHours(2));
        return new CreateAccessTokenResDto(token);
    }
}
