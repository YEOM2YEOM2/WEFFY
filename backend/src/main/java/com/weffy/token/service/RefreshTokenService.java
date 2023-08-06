package com.weffy.token.service;

import com.weffy.token.dto.response.CreateTokenResDto;
import com.weffy.user.entity.WeffyUser;
import jakarta.servlet.http.HttpServletRequest;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.model.User;

public interface RefreshTokenService {

    void saveToken(WeffyUser weffyUser, String refreshToken);
    CreateTokenResDto createUserToken(HttpServletRequest request,  ApiResponse<User> userInfo, WeffyUser weffyUser);
}
