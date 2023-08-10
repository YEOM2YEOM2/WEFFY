package com.weffy.token.service;

import com.weffy.token.dto.response.CreateTokenResDto;
import com.weffy.user.entity.WeffyUser;
import jakarta.servlet.http.HttpServletRequest;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.model.User;

import java.io.IOException;

public interface RefreshTokenService {

    void saveToken(WeffyUser weffyUser, String refreshToken);
    CreateTokenResDto createUserToken(ApiResponse<User> userInfo, WeffyUser weffyUser) throws IOException, InterruptedException;
}
