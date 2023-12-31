package com.weffy.user;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.token.util.SecurityUtil;
import com.weffy.user.dto.Request.UserSignInReqDto;
import com.weffy.user.dto.Response.UserMainResDto;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import com.weffy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "1. User API", description = "weffy user api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "회원가입", description = "weffy에 해당 User의 정보가 없으면 회원가입 후 로그인 \n\n" )
    @Parameter(name = "role", description = "추가하지 않을 시 default로 USER, \n\n admin을 등록할 시 ADMIN", required = false , in = ParameterIn.QUERY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "CREATED", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 201,\n" +
                            "    \"data\": \"CREATED\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "4000", description =  "USER_EXIST", content = @Content(examples = @ExampleObject(value = "{\"status\": 4000, \"data\": \"회원이 존재합니다.\"}"))),
            @ApiResponse(responseCode = "4002", description =  "USER_WITHDRAW", content = @Content(examples = @ExampleObject(value = "{\"status\": 4002, \"data\": \"탈퇴한 회원입니다.\"}"))),
            @ApiResponse(responseCode = "4004", description =  "MATTERMOST_LOGIN_FAILED", content = @Content(examples = @ExampleObject(value = "{\"status\": 4004, \"data\": \"mattermost 로그인 실패\"}"))),
            @ApiResponse(responseCode = "4005", description =  "IMAGE_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4005, \"data\": \"mattermost 이미지가 존재하지 않습니다.\"}"))),
    })
    @PostMapping("/signup")
    public ResponseEntity<? extends BaseResponseBody> signup(@RequestBody UserSignInReqDto signinInfo, @RequestParam(name = "role", required = false) String role ) throws IOException {
        userService.signUp(signinInfo, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, "CREATED"));
    }

    @Operation(summary = "로그인", description = "weffy에 해당 User의 정보를 찾아 로그인 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK" , content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"identification\": \"identification\",\n" +
                            "        \"nickName\": \"nickName\",\n" +
                            "        \"profileImg\": \"https://weffy.s3.ap-northeast-2.amazonaws.com/identification.png\",\n" +
                            "        \"token\": {\n" +
                            "            \"accessToken\": \"accessToken\",\n" +
                            "            \"refreshToken\": \"refreshToken\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4001", description =  "USER_NOT_EXIST", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"회원이 존재하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "4002", description =  "USER_WITHDRAW", content = @Content(examples = @ExampleObject(value = "{\"status\": 4002, \"data\": \"탈퇴한 회원입니다.\"}"))),
            @ApiResponse(responseCode = "4004", description =  "MATTERMOST_LOGIN_FAILED", content = @Content(examples = @ExampleObject(value = "{\"status\": 4004, \"data\": \"mattermost 로그인 실패\"}"))),
    })
    @PostMapping("/signin")
    public ResponseEntity<? extends BaseResponseBody> signin(@RequestBody UserSignInReqDto signinInfo ) throws IOException, InterruptedException {
        UserSignInResDto weffyUser = userService.signIn(signinInfo);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, weffyUser));
    }

    @Operation(summary = "User 회원 조회 in main", description = "main에서 필요한 회원 정보 조회 \n\n" )
    @Parameter(name = "identification", description = "고유 ID값", required =  true , in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"identification\": \"identification\",\n" +
                            "        \"nickName\": \"nickName\",\n" +
                            "        \"profileImg\": \"https://weffy.s3.ap-northeast-2.amazonaws.com/identification.png\"\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4001", description =  "USER_NOT_EXIST", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"회원이 존재하지 않습니다.\"}"))),
    })
    @GetMapping("/{identification}")
    public ResponseEntity<? extends BaseResponseBody> mainUser(@PathVariable String identification) {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(authorizedMember);
        if (weffyUser.isPresent() && weffyUser.get().getIdentification().equals(identification)) {
            UserMainResDto userinfo = userService.mainUser(identification);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, userinfo));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(400, new CustomException(ExceptionEnum.INVALID_USER)));
    }

    @Operation(summary = "User 회원 조회", description = "회원 정보 조회 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"identification\": \"identification\",\n" +
                            "        \"email\": \"email\",\n" +
                            "        \"name\": \"name\",\n" +
                            "        \"nickname\": \"nickname\",\n" +
                            "        \"profileImg\": \"https://weffy.s3.ap-northeast-2.amazonaws.com/identification.png\",\n" +
                            "        \"role\": \"USER\",\n" +
                            "        \"active\": true\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4001", description =  "USER_NOT_EXIST", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"회원이 존재하지 않습니다.\"}"))),
    })
    @GetMapping("/me")
    public ResponseEntity<? extends BaseResponseBody> userInfo() {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(authorizedMember);
        if (weffyUser.isPresent() ) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, userService.getUser(weffyUser.get())));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(400, new CustomException(ExceptionEnum.INVALID_USER)));
    }

    @Operation(summary = "User 회원 정보 수정", description = "profile image와 nickname 수정 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": \"success\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "4001", description =  "USER_NOT_EXIST", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"회원이 존재하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "4006", description =  "FILE_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4006, \"data\": \"파일이 존재하지 않습니다.\"}"))),
    })
    @PatchMapping("/me")
    public ResponseEntity<? extends BaseResponseBody> updateUser(@RequestPart(name = "profileImg", required = false) MultipartFile profileImg, @RequestPart(name = "nickName", required = false) String nickName) {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(authorizedMember);
        if (weffyUser.isPresent() ) {
            userService.setUser(weffyUser.get(), profileImg, nickName);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "success"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(400, new CustomException(ExceptionEnum.INVALID_USER)));
    }

    @Operation(summary = "User 회원 탈퇴", description = "회원 탈퇴 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": \"success\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "4001", description =  "USER_NOT_EXIST", content = @Content(examples = @ExampleObject(value = "{\"status\": 4001, \"data\": \"회원이 존재하지 않습니다.\"}"))),
    })
    @PatchMapping("/withdraw")
    public ResponseEntity<? extends BaseResponseBody> deleteUser() {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(authorizedMember);
        if (weffyUser.isPresent() ) {
            userService.deleteUser(weffyUser.get());
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "success"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(400, new CustomException(ExceptionEnum.INVALID_USER)));
    }


}
