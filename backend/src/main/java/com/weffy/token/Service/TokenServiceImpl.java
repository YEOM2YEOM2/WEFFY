package com.weffy.token.Service;

import com.weffy.token.Dto.Response.CreateAccessTokenResDto;
import com.weffy.token.TokenProvider;
import com.weffy.user.Entity.WeffyUser;
import com.weffy.user.Service.UserService;
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
