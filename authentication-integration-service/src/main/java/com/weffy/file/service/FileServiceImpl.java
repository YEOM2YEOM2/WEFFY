package com.weffy.file.service;


import com.weffy.file.dto.response.FileResDto;
import com.weffy.file.entity.Files;
import com.weffy.file.repository.JpaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service("FileService")
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final JpaFileRepository jpaFileRepository;
    private final S3Client s3Client;

    @Override
    public FileResDto uploadFile(MultipartFile file, String conferenceId, String bucketName) {

        String fileName = file.getOriginalFilename();
        String type = file.getContentType();

        String encodedFileName;
        try {
//             한글 인코딩
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentDisposition("inline")
                            .contentType(type)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new IllegalStateException("파일 업로드 실패", e);
        }

        String url = String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, encodedFileName.toLowerCase());
        Files files = saveFileToDatabase(fileName, url, conferenceId, file.getSize());

        return new FileResDto().of(files);
    }

    private Files saveFileToDatabase(String title, String url, String conferenceId, long size) {
        Files files = Files.builder()
                .title(title)
                .url(url)
                .conferenceId(conferenceId)
                .size(size)
                .build();

        return jpaFileRepository.save(files);
    }
    @Override
    public String uploadInputStream(BufferedImage image, String fileName, String bucketName) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        byte[] buffer = os.toByteArray();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentDisposition("inline")
                .contentType("image/png")
                .build();
        s3Client.putObject(putRequest, RequestBody.fromBytes(buffer));

        return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, fileName.toLowerCase());
    }
}

