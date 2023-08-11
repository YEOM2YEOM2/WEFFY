package com.weffy.mattermostcontentservice.file;

import com.weffy.mattermostcontentservice.file.dto.FileDto;
import com.weffy.mattermostcontentservice.file.dto.GetFileReqDto;
import com.weffy.mattermostcontentservice.file.service.FileService;
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
public class FileController {
    private final FileService fileService;

    private final Logger logger = LoggerFactory.getLogger(FileController.class);


    @PostMapping("/uploadFilesToMattermost")
    public ResponseEntity<String> uploadFiles(@RequestBody GetFileReqDto getFileDto) {
        try {
            String sessionToken = getFileDto.getSessionToken();
            String classId = getFileDto.getClassId();
            List<FileDto> files = getFileDto.getFiles();

            fileService.uploadFilesMM(sessionToken,classId,files);

            return ResponseEntity.ok("Files sent to Mattermost");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed: " + e.getMessage());
        }
    }

}
