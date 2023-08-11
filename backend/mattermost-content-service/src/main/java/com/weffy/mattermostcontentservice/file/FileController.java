package com.weffy.mattermostcontentservice.file;

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
//@Tag(name = "File Operations", description = "APIs related to file operations")
public class FileController {
    private final FileService fileService;

    private final Logger logger = LoggerFactory.getLogger(FileController.class);


    @PostMapping("/uploadFilesToMattermost")
//    @Operation(summary = "Upload files to Mattermost",
//            description = "Accepts a session token, class ID and a list of files to upload to Mattermost.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Files successfully sent to Mattermost"),
//            @ApiResponse(responseCode = "400", description = "Failed to send files")
//    })
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
