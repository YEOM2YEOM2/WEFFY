package com.weffy.file;

import com.weffy.common.dto.BaseResponseBody;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.file.dto.request.FileReqDto;
import com.weffy.file.dto.response.FileResDto;
import com.weffy.file.dto.response.GetFileDto;
import com.weffy.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;

    @PostMapping("/{conferenceId}")
    public ResponseEntity<? extends BaseResponseBody> upload(@RequestPart MultipartFile file, @PathVariable(required = false) String conferenceId) {
        String bucketName = (conferenceId == null) ? "weffy" : "weffy-conference";
        FileResDto fileResDto = fileService.uploadFile(file, conferenceId, bucketName);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, fileResDto));
    }

    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> upload(@RequestBody FileReqDto fileReqDto) {
        List<GetFileDto> getFileDto = fileService.getFiles(fileReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(200, getFileDto));
    }

    @GetMapping("/download")
    public ResponseEntity<? extends BaseResponseBody> downloadFile(@RequestParam String url, @RequestParam String filename) {
        try {
            fileService.fileDownload(url, filename);
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(200, "SUCCESS"));
        } catch (IOException e) {
            return  ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(400, new CustomException(ExceptionEnum.FILENOTFOUND)));
        }
    }
}
