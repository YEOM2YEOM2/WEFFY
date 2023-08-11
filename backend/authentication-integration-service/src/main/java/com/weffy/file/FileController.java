package com.weffy.file;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.file.dto.request.FileReqDto;
import com.weffy.file.dto.response.FileResDto;
import com.weffy.file.dto.response.GetFileDto;
import com.weffy.file.dto.response.UploadResDto;
import com.weffy.file.service.FileService;
import com.weffy.mattermost.repository.JpaSessionRepository;
import com.weffy.token.util.SecurityUtil;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserRepository userRepository;
    private final JpaSessionRepository jpaSessionRepository;

    @PostMapping("/{conferenceId}")
    public ResponseEntity<? extends BaseResponseBody> upload(@RequestPart MultipartFile file, @PathVariable(required = false) String conferenceId) {
        String bucketName = (conferenceId == null) ? "weffy" : "weffy-conference";
        FileResDto fileResDto = fileService.uploadFile(file, conferenceId, bucketName);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, fileResDto));
    }

    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> upload(@RequestBody FileReqDto fileReqDto) {
        String authorizedMember = SecurityUtil.getAuthorizedMember();
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(authorizedMember);
        String sessionToken = weffyUser.map(user -> jpaSessionRepository.findByWeffyUser(user).get().getToken()).orElse(null);
        List<GetFileDto> getFileDto = fileService.getFiles(fileReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(200, new UploadResDto().of(sessionToken, fileReqDto.getConferenceId(), getFileDto)));
    }

    @GetMapping("/download")
    public ResponseEntity<? extends BaseResponseBody> downloadFile(@RequestParam String objectKey, @RequestParam String title) {
        fileService.downloadFile(objectKey, title);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "SUCCESS"));
    }
}
