package com.weffy.file.service;

import com.weffy.file.dto.response.FileResDto;
import com.weffy.file.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface FileService {

    // MuiltipartFile S3에 업로드 후 URL 반환
    FileResDto uploadFile(MultipartFile file, String conferenceId, String bucketName);

    // Mattermost에서 받은 프로필 이미지 S3에 업로드 후 URL 반환
    String uploadInputStream(BufferedImage image, String fileName, String bucketName) throws IOException;

}
