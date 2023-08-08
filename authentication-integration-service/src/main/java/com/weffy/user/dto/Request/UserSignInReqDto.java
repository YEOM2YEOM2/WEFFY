package com.weffy.user.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInReqDto {
    String email;
    String password;
}