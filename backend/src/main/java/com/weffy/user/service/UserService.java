package com.weffy.user.service;

import com.weffy.user.dto.Request.UserSignInReqDto;
import com.weffy.user.dto.Response.UserInfoResDto;
import com.weffy.user.dto.Response.UserMainResDto;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.WeffyUser;

public interface UserService {

    UserSignInResDto signIn(UserSignInReqDto signinInfo, String role);
    WeffyUser findById(Long userId);

    UserMainResDto mainUser(String userId);

}