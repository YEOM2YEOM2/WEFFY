package com.weffy.user.service;

import com.weffy.user.dto.Request.UserSignInReqDto;
import com.weffy.user.dto.Response.UserInfoResDto;
import com.weffy.user.dto.Response.UserMainResDto;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.WeffyUser;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserSignInResDto signUp(HttpServletRequest request, UserSignInReqDto signinInfo, String role);
    UserSignInResDto signIn(HttpServletRequest request, UserSignInReqDto signinInfo);
    WeffyUser findById(Long userId);

    UserMainResDto mainUser(String userId);
    UserInfoResDto getUser(WeffyUser weffyUser);
}