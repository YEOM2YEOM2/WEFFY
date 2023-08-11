package com.weffy.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface FileService {

    // MuiltipartFile S3에 업로드 후 URL 반환
    String uploadFile(MultipartFile file);

    // Mattermost에서 받은 프로필 이미지 S3에 업로드 후 URL 반환
    String uploadInputStream(BufferedImage image, String fileName) throws IOException;
}
