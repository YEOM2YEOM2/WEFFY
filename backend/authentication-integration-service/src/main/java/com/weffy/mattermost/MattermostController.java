package com.weffy.mattermost;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.mattermost.dto.response.TeamChannelResDto;
import com.weffy.mattermost.service.MattermostService;
import com.weffy.token.util.SecurityUtil;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import com.weffy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mattermost")
public class MattermostController {

    private final UserService userService;
    private final MattermostService mattermostService;

    @Operation(summary = "team & channel 조회", description = "user가 속해있는 team & channel 조회 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK"),
            @ApiResponse(responseCode = "401", description =  "요청된 리소스에 대한 유효한 인증 자격 증명"),
            @ApiResponse(responseCode = "403", description =  "서버에서 설정해 둔 권한과 맞지 않는 접속 요청"),
            @ApiResponse(responseCode = "404", description =  "잘못된 요청으로 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description =  "서버 오류")
    })
    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> teamChannel() {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        WeffyUser weffyUser = userService.findByEmail(authorizedMember);
        List<TeamChannelResDto> teamChannelResDto = mattermostService.getTeamAndChannel(weffyUser);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, teamChannelResDto));
    }

    @PostMapping("/header")
    public ResponseEntity<? extends BaseResponseBody> makeHeaderLink(@RequestPart(name = "channelId") String channelId) {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        WeffyUser weffyUser = userService.findByEmail(authorizedMember);
        if (mattermostService.makeHeaderLink(weffyUser, channelId) == 200) return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "SUCCESS"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(407, new CustomException(ExceptionEnum.HEADER_MODIFICATION_FAILED)));
    }


}
