package com.weffy.mattermostcontentservice.file;

import com.weffy.mattermostcontentservice.common.dto.BaseResponseBody;
import com.weffy.mattermostcontentservice.file.dto.FileDto;
import com.weffy.mattermostcontentservice.file.dto.GetFileReqDto;
import com.weffy.mattermostcontentservice.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v4/files")
@CrossOrigin("*")
@Tag(name = "File Operations", description = "APIs related to file operations")
public class FileController {
    private final FileService fileService;

    private final Logger logger = LoggerFactory.getLogger(FileController.class);


    @PostMapping("/uploadFilesToMattermost")
    @Operation(summary = "mm채널로 파일보내기",
            description = "선택한 파일들의 s3URL 링크를 받아 다운로드하고 MM해당 채널로 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "파일 전송 실패")
    })
    public ResponseEntity<? extends BaseResponseBody> uploadFiles(@RequestBody GetFileReqDto getFileDto) {
        try {
            String sessionToken = getFileDto.getSessionToken();
            String classId = getFileDto.getClassId();
            List<FileDto> files = getFileDto.getFiles();

            fileService.uploadFilesMM(sessionToken,classId,files);

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, sessionToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(400, "서버 오류"));
        }
    }

}
