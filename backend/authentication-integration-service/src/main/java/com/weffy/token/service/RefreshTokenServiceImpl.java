package com.weffy.token.service;

import com.weffy.mattermost.MattermostHandler;
import com.weffy.mattermost.service.MattermostService;
import com.weffy.token.config.TokenProvider;
import com.weffy.token.dto.response.CreateTokenResDto;
import com.weffy.token.entity.RefreshToken;
import com.weffy.token.repository.RefreshTokenRepository;
import com.weffy.user.entity.WeffyUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.model.User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;


@Service("refreshTokenService")
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final MattermostService mattermostService;


    @Override
    @Transactional
    public void saveToken(WeffyUser weffyUser, String refreshToken) {
        RefreshToken token = RefreshToken
                .builder()
                .weffyUser(weffyUser)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }


    @Override
    public CreateTokenResDto createUserToken(ApiResponse<User> userInfo, WeffyUser weffyUser) throws IOException, InterruptedException {
        // Mattermost 세션 토큰
        String token = Objects.requireNonNull(userInfo.getRawResponse().getHeaders().get("Token").get(0).toString());
        mattermostService.saveSession(weffyUser, token);
        mattermostService.saveTeam(weffyUser.getIdentification(), token);
        // accessToken
        String accessToken = tokenProvider.generateToken(weffyUser,  Duration.ofHours(1));
        //  refreshToken
        String refreshToken = tokenProvider.generateToken(weffyUser,  Duration.ofDays(14));
        Optional<RefreshToken> beforeToken = refreshTokenRepository.findByWeffyUser(weffyUser);
        if ((beforeToken.isPresent())) {
            beforeToken.get().updateToken(refreshToken);
        } else {
            saveToken(weffyUser, refreshToken);
        }
        return new CreateTokenResDto().of(accessToken, refreshToken);
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
