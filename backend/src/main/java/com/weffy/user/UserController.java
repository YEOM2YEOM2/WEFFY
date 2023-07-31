package com.weffy.user;

import com.weffy.BaseResponseBody;
import com.weffy.user.Dto.Request.UserSignInReqDto;
import com.weffy.user.Dto.Response.UserSignInResDto;
import com.weffy.user.Entity.Role;
import com.weffy.user.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "weffy user api")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "로그인 및 회원가입", description = "weffy에 해당 User의 정보가 없으면 회원가입 후 로그인, 있으면 바로 로그인 \n\n" )
    @Parameter(name = "role", description = "추가하지 않을 시 default로 USER, \n\n admin을 등록할 시 ADMIN", required = false , in = ParameterIn.QUERY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK"),
            @ApiResponse(responseCode = "201", description =  "CREATED"),
            @ApiResponse(responseCode = "401", description =  "요청된 리소스에 대한 유효한 인증 자격 증명"),
            @ApiResponse(responseCode = "403", description =  "서버에서 설정해 둔 권한과 맞지 않는 접속 요청"),
            @ApiResponse(responseCode = "404", description =  "잘못된 요청으로 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description =  "서버 오류")
    })
    @PostMapping("/signin")
    public ResponseEntity<? extends BaseResponseBody> signin(@RequestBody UserSignInReqDto signinInfo, @RequestParam(name = "role", required = false) String role ) {
        UserSignInResDto weffyUser = userService.signIn(signinInfo, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, weffyUser));
    }
}
