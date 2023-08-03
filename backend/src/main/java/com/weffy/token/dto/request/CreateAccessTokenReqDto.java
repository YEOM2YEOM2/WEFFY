package com.weffy.token.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenReqDto {
    private String refreshToken;
}
