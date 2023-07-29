package com.weffy.user.Dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResDto {
    String userId;
    String profile_img;
    String token;

    public UserSignInResDto of(String userId, String profile_img, String token) {
        UserSignInResDto res = new UserSignInResDto();
        res.userId = userId;
        res.profile_img = profile_img;
        res.token = token;
        return res;
    }
}