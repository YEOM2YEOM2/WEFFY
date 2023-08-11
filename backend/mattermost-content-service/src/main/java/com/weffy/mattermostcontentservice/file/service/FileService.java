package com.weffy.mattermostcontentservice.file.service;

import com.weffy.mattermostcontentservice.file.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FileService {
    void uploadFilesMM(String sessionToken, String classId, List<FileDto> files) throws IOException, URISyntaxException;
}
