package com.weffy.user.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResDto {
    String userId;
    String accessToken;
    String refreshToken;

    public UserSignInResDto of(String userId, String accessToken, String refreshToken) {
        UserSignInResDto res = new UserSignInResDto();
        res.userId = userId;
        res.accessToken = accessToken;
        res.refreshToken = refreshToken;
        return res;
    }
}