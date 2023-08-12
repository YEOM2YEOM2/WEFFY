package com.weffy.mattermost;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.mattermost.dto.response.TeamChannelResDto;
import com.weffy.mattermost.service.MattermostService;
import com.weffy.token.util.SecurityUtil;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mattermost")
@Tag(name = "3. Mattermost API", description = "weffy mattermost api")
public class MattermostController {

    private final UserService userService;
    private final MattermostService mattermostService;

    @Operation(summary = "Mattermost 헤더 링크 생성", description = "해당 채널의 회의 룸을 Mattermost 헤더에 저장 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": \"success\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "4007", description =  "HEADER_MODIFICATION_FAILED", content = @Content(examples = @ExampleObject(value = "{\"status\": 4007, \"data\": \"헤더 변경을 실패하였습니다.\"}"))),
            @ApiResponse(responseCode = "4008", description =  "CHANNEL_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4008, \"data\": \"채널이 존재하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "4009", description =  "CANNOT_CREATE_ROOM", content = @Content(examples = @ExampleObject(value = "{\"status\": 4009, \"data\": \"해당 채널에서 weffy를 생성할 권한이 없습니다.\"}"))),
    })
    @PostMapping("/header")
    public ResponseEntity<? extends BaseResponseBody> makeHeaderLink(@RequestPart(name = "channelId") String channelId) {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        WeffyUser weffyUser = userService.findByEmail(authorizedMember);
        if (mattermostService.makeHeaderLink(weffyUser, channelId) == 200) return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "SUCCESS"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(407, new CustomException(ExceptionEnum.HEADER_MODIFICATION_FAILED)));
    }


    @Operation(summary = "team & channel 조회", description = "user가 속해있는 team & channel 조회 \n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": [\n" +
                            "        {\n" +
                            "            \"id\": 1,\n" +
                            "            \"identification\": \"bq3kpm86jfgk3dx8uxoc577r5c\",\n" +
                            "            \"name\": \"9기 공통 구미1반\",\n" +
                            "            \"channels\": [\n" +
                            "                {\n" +
                            "                    \"id\": 1,\n" +
                            "                    \"identification\": \"1gd7utcti3bwbki1f8d4n6ws8a\",\n" +
                            "                    \"name\": \"D107\",\n" +
                            "                    \"admin\": false\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 2,\n" +
                            "                    \"identification\": \"xezputbgu3rmdyckjssqpn8jwc\",\n" +
                            "                    \"name\": \"D107_BOT\",\n" +
                            "                    \"admin\": false\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 3,\n" +
                            "                    \"identification\": \"r7ammn95k7gzj8fekdspzk1t1r\",\n" +
                            "                    \"name\": \"UCC담당자\",\n" +
                            "                    \"admin\": false\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 4,\n" +
                            "                    \"identification\": \"9kj6a3s7rjyi9p8f93ydezcuth\",\n" +
                            "                    \"name\": \"공지사항\",\n" +
                            "                    \"admin\": false\n" +
                            "                },\n" +
                            "                {\n" +
                            "                    \"id\": 5,\n" +
                            "                    \"identification\": \"cufyzsupftyj8jojg9ijsugbfo\",\n" +
                            "                    \"name\": \"잡담\",\n" +
                            "                    \"admin\": false\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}"))),
            @ApiResponse(responseCode = "4008", description =  "CHANNEL_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4008, \"data\": \"채널이 존재하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "4009", description =  "CANNOT_CREATE_ROOM", content = @Content(examples = @ExampleObject(value = "{\"status\": 4009, \"data\": \"해당 채널에서 weffy를 생성할 권한이 없습니다.\"}"))),
    })
    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> teamChannel() {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        WeffyUser weffyUser = userService.findByEmail(authorizedMember);
        List<TeamChannelResDto> teamChannelResDto = mattermostService.getTeamAndChannel(weffyUser);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, teamChannelResDto));
    }

}
