package com.weffy.file;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.file.dto.request.FileReqDto;
import com.weffy.file.dto.response.FileResDto;
import com.weffy.file.dto.response.GetFileDto;
import com.weffy.file.dto.response.UploadResDto;
import com.weffy.file.service.FileService;
import com.weffy.mattermost.repository.JpaSessionRepository;
import com.weffy.mattermost.service.MattermostService;
import com.weffy.token.util.SecurityUtil;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
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
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Tag(name = "4. File API", description = "weffy file api")
public class FileController {
    private final FileService fileService;
    private final UserService userService;
    private final MattermostService mattermostService;

    @Operation(summary = "file 업로드", description = "파일을 s3와 db에 저장\n\n " +
            "(default : 프로필 이미지 업로드 - weffy)\n\n" +
            "(conferenceId 추가 시 : 강의 내의 파일 업로드, 다운로드 - weffy-conference)\n\n" +
            "(type=lecture 추가 시: 강의 내의 녹화 파일 업로드, 다운로드 - weffy-lecture)\n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "CREATED", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 201,\n" +
                            "    \"data\": {\n" +
                            "        \"url\": \"https://weffy-conference.s3.ap-northeast-2.amazonaws.com/file.PNG\",\n" +
                            "        \"title\": \"file.PNG\",\n" +
                            "        \"objectKey\": \"objectKey\",\n" +
                            "        \"conferenceId\": \"conferenceId\"\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4007", description =  "CANNOT_UPLOAD_FILE", content = @Content(examples = @ExampleObject(value = "{\"status\": 4007, \"data\": \"파일 업로드에 실패하였습니다.\"}"))),
            @ApiResponse(responseCode = "4008", description =  "CHANNEL_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4008, \"data\": \"채널이 존재하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "4009", description =  "CANNOT_CREATE_ROOM", content = @Content(examples = @ExampleObject(value = "{\"status\": 4009, \"data\": \"해당 채널에서 weffy를 생성할 권한이 없습니다.\"}"))),
    })
    @PostMapping("/upload/{conference_id}")
    public ResponseEntity<? extends BaseResponseBody> upload(@RequestPart MultipartFile file,
                                                             @PathVariable(name = "conference_id", required = false) String conferenceId) {
        String bucketName = (conferenceId == null) ? "weffy" : "weffy-conference";
        FileResDto fileResDto = fileService.uploadFile(file, conferenceId, bucketName);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, fileResDto));
    }

    @Operation(summary = "file 조회", description = "해당하는 회의 중 저장된 파일 불러오기 \n\n" +
            "(conferenceId : conferenceId)\n\n" +
            "(start : 강의 시작 시간 - 2023-08-18T00:00:00)\n\n" +
            "(end : 강의 종료 시간 - 2023-08-18T00:00:00)\n\n" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"sessionToken\": \"sessionToken\",\n" +
                            "        \"classId\": \"conferenceId\",\n" +
                            "        \"files\": [\n" +
                            "            {\n" +
                            "                \"fileUrl\": \"https://weffy-conference.s3.ap-northeast-2.amazonaws.com/fileUrl.PNG\",\n" +
                            "                \"fileName\": \"fileName.PNG\"\n" +
                            "            }\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}"))),
            @ApiResponse(responseCode = "4006", description =  "FILE_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4006, \"data\": \"파일이 존재하지 않습니다.\"}")))
    })
    @PostMapping("/list")
    public ResponseEntity<? extends BaseResponseBody> getFiles(@RequestBody FileReqDto fileReqDto) {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        WeffyUser weffyUser = userService.findByEmail(authorizedMember);
        String sessionToken = mattermostService.findByWeffyUser(weffyUser);
        List<GetFileDto> getFileDto = fileService.getFiles(fileReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, new UploadResDto().of(sessionToken, fileReqDto.getConferenceId(), getFileDto)));
    }

    @Operation(summary = "file 다운로드", description = "s3에 저장된 파일 다운로드 \n\n" +
            "ex) https://i9d107.p.ssafy.io/file/download?objectKey=objectKey&title=title\n\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserSignInResDto.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": \"success\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "4006", description =  "FILE_NOT_FOUND", content = @Content(examples = @ExampleObject(value = "{\"status\": 4006, \"data\": \"파일이 존재하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "4008", description =  "CANNOT_DOWNLOAD_FILE", content = @Content(examples = @ExampleObject(value = "{\"status\": 4008, \"data\": \"파일 다운로드 실패하였습니다.\"}")))
    })
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String objectKey, @RequestParam String title) throws IOException {
        return fileService.downloadFile(objectKey, title);
        //return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "SUCCESS"));
    }

    @PostMapping("/uploadMM")
    public ResponseEntity<? extends BaseResponseBody> uploadMM(@RequestBody FileReqDto fileReqDto)  {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        WeffyUser weffyUser = userService.findByEmail(authorizedMember);
        String sessionToken = mattermostService.findByWeffyUser(weffyUser);
        List<GetFileDto> getFileDto = fileService.getFiles(fileReqDto);

        // Set up RestTemplate and data to send
        RestTemplate restTemplate = new RestTemplate();
        String targetUrl = "http://i9d107.p.ssafy.io:8084/api/v4/files/uploadFilesToMattermost";

        UploadResDto resDto = new UploadResDto();
        resDto.setSessionToken(sessionToken);
        resDto.setClassId(fileReqDto.getConferenceId());
        resDto.setFiles(getFileDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  // Setting the Content-Type to JSON

        HttpEntity<UploadResDto> entity = new HttpEntity<>(resDto, headers);

        // Send POST request to the other server
        restTemplate.postForEntity(targetUrl, entity, String.class);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, new UploadResDto().of(sessionToken, fileReqDto.getConferenceId(), getFileDto)));

    }
}
