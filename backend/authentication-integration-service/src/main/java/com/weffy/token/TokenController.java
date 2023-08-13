package com.weffy.token;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.token.dto.request.CreateAccessTokenReqDto;
import com.weffy.token.dto.response.CreateAccessTokenResDto;
import com.weffy.token.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "2. Token API", description = "weffy token api")
public class TokenController {
    @Autowired
    TokenService tokenService;

    @Operation(summary = "Access 토큰 재발급", description = "weffy의  Refresh Token을 통해 Access Token 재발급\n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "CREATED", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 201,\n" +
                            "    \"data\": {\n" +
                            "        \"accessToken\": \"newAccessToken\"\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4000", description =  "USER_EXIST", content = @Content(examples = @ExampleObject(value = "{\"status\": 4000, \"data\": \"회원이 존재합니다.\"}"))),
            @ApiResponse(responseCode = "4002", description =  "USER_WITHDRAW", content = @Content(examples = @ExampleObject(value = "{\"status\": 4002, \"data\": \"탈퇴한 회원입니다.\"}"))),
            @ApiResponse(responseCode = "4004", description =  "MATTERMOST_LOGIN_FAILED", content = @Content(examples = @ExampleObject(value = "{\"status\": 4004, \"data\": \"mattermost 로그인 실패\"}"))),
            @ApiResponse(responseCode = "4005", description =  "IMAGE_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4005, \"data\": \"mattermost 이미지가 존재하지 않습니다.\"}"))),
    })
    @PostMapping("/token")
    public ResponseEntity<? extends BaseResponseBody> createNewAccessToken(@RequestBody CreateAccessTokenReqDto refreshToken) {
        CreateAccessTokenResDto newAccessToken = tokenService.createNewAccessToken(refreshToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, newAccessToken));
    }

}
