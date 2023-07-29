package com.weffy.user.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInReqDto {
    String email;
    String password;
}