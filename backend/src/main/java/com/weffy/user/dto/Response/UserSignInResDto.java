package com.weffy.user.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResDto {
    String identification;
    String accessToken;
    String refreshToken;

    public UserSignInResDto of(String identification, String accessToken, String refreshToken) {
        UserSignInResDto res = new UserSignInResDto();
        res.identification = identification;
        res.accessToken = accessToken;
        res.refreshToken = refreshToken;
        return res;
    }
}