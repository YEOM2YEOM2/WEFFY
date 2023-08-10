package com.weffy.file.service;

import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.file.dto.response.FileResDto;
import com.weffy.file.entity.Files;
import com.weffy.file.repository.JpaFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service("FileService")
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final JpaFileRepository jpaFileRepository;

    private final S3Client s3Client;
    private final String bucketName = "weffy";

    @Override
    public FileResDto uploadFile(MultipartFile file,  String conferenceId) {
        String fileName = file.getOriginalFilename();
        String type = file.getContentType();

        String encodedFileName;
        try {
//             한글 인코딩
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%2B");
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
        String url = String.format("https://weffy.s3.ap-northeast-2.amazonaws.com/%s", fileName.toLowerCase());
        Files files = Files.builder()
                .title(encodedFileName)
                .url(url)
                .conferenceId(conferenceId)
                .size(file.getSize())
                .build();
        jpaFileRepository.save(files);
        FileResDto fileResDto = new FileResDto().of(files);
        return fileResDto;
    }


    @Override
    public String uploadInputStream(BufferedImage image, String fileName) throws IOException {
        try {
            // BufferedImage를 바이트 배열로 변환합니다.
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os); // 원하는 이미지 형식을 선택합니다 (png, jpg 등)
            byte[] buffer = os.toByteArray();

            S3Client s3Client = S3Client.builder().region(Region.AP_NORTHEAST_2).build();

            // S3에 바이트 배열을 업로드합니다.
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentDisposition("inline")
                    .contentType("image/png")
                    .build(); //.contentDisposition("inline") 브라우저 열기
            s3Client.putObject(putRequest, RequestBody.fromBytes(buffer));

            System.out.println("Image uploaded successfully");
        } catch (IOException e) {
            throw new CustomException(ExceptionEnum.IMAGENOTFOUND);
        }

        return String.format("https://weffy.s3.ap-northeast-2.amazonaws.com/%s", fileName.toLowerCase());
    }
}

