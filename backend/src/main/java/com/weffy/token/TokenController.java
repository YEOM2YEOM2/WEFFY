package com.weffy.token;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.token.dto.request.CreateAccessTokenReqDto;
import com.weffy.token.dto.response.CreateAccessTokenResDto;
import com.weffy.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TokenController {
    @Autowired
    TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<? extends BaseResponseBody> createNewAccessToken(@RequestBody CreateAccessTokenReqDto refreshToken) {
        CreateAccessTokenResDto newAccessToken = tokenService.createNewAccessToken(refreshToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, newAccessToken));
    }

}
