package com.weffy.user.dto.Response;

import com.weffy.token.dto.response.CreateTokenResDto;
import com.weffy.user.entity.WeffyUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResDto {
    String identification;
    String nickName;
    String profileImg;
    CreateTokenResDto token;

    public UserSignInResDto of(WeffyUser weffyUser, CreateTokenResDto createTokenResDto) {
        UserSignInResDto res = new UserSignInResDto();
        res.identification = weffyUser.getIdentification();
        res.nickName = weffyUser.getNickname();
        res.profileImg = weffyUser.getProfileImg();
        res.token = createTokenResDto;
        return res;
    }
}