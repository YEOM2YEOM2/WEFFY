package com.weffy.user.Service;

import com.weffy.user.Dto.Request.UserSignInReqDto;
import com.weffy.user.Dto.Response.UserSignInResDto;
import com.weffy.user.Entity.Role;
import com.weffy.user.Entity.WeffyUser;

public interface UserService {

    UserSignInResDto signIn(UserSignInReqDto signinInfo, String role);
    WeffyUser findById(Long userId);
}