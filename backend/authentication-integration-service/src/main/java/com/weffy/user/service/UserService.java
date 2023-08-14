package com.weffy.user.service;

import com.weffy.user.dto.Request.UserSignInReqDto;
import com.weffy.user.dto.Response.UserInfoResDto;
import com.weffy.user.dto.Response.UserMainResDto;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.Role;
import com.weffy.user.entity.WeffyUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    // 회원가입
    void signUp(UserSignInReqDto signinInfo, String role) throws IOException;
    // 회원 가입 시 Mattermost nickname으로 부터 권한 부여
    Role getRole(String nickName);
    // 로그인
    UserSignInResDto signIn(UserSignInReqDto signinInfo) throws IOException, InterruptedException;
    // repository 예외 처리
    WeffyUser findById(Long userId);
    WeffyUser findByEmail(String email);
    // 메인화면에서 필요한 user 정보 조회
    UserMainResDto mainUser(String userId);
    // user 정보 조회
    UserInfoResDto getUser(WeffyUser weffyUser);
    // 이미지와 nickname 수정
    void setUser(WeffyUser weffyUser, MultipartFile profileImg, String nickName);
    // password 변경
    void setPassword(WeffyUser weffyUser, String password);
    // 회원 탈퇴(비활성)
    void deleteUser(WeffyUser weffyUser);
}