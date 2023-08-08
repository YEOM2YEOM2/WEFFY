package com.weffy.file;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.file.service.FileService;
import com.weffy.user.dto.Request.UserSignInReqDto;
import com.weffy.user.dto.Response.UserSignInResDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;

    @PostMapping("")
    public ResponseEntity<? extends BaseResponseBody> upload(@RequestPart MultipartFile file) throws IOException {
        String fileUrl = fileService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, fileUrl));
    }
}
