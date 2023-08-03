package com.weffy.user.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResDto {
    String userId;
    String profile_img;
    String accessToken;
    String refreshToken;

    public UserSignInResDto of(String userId, String profile_img, String accessToken, String refreshToken) {
        UserSignInResDto res = new UserSignInResDto();
        res.userId = userId;
        res.profile_img = profile_img;
        res.accessToken = accessToken;
        res.refreshToken = refreshToken;
        return res;
    }
}