package com.weffy.file.service;

import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.file.entity.Files;
import com.weffy.file.dto.request.FileReqDto;
import com.weffy.file.dto.response.FileResDto;
import com.weffy.file.dto.response.GetFileDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.weffy.file.repository.JpaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service("fileService")
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final JpaFileRepository jpaFileRepository;
    private final S3Client s3Client;

    @Override
    public FileResDto uploadFile(MultipartFile file, String conferenceId, String bucketName) {

        String originalFileName = file.getOriginalFilename();
        String type = file.getContentType();

        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        String encodedFileName;
        try {
            // 한글 인코딩
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
            throw new CustomException(ExceptionEnum.CANNOT_UPLOAD_FILE);
        }

        String url = String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, encodedFileName);
        Files files = saveFileToDatabase(originalFileName, url, conferenceId, fileName);

        return new FileResDto().of(files);
    }


    private Files saveFileToDatabase(String title, String url, String conferenceId, String objectKey) {
        Files files = Files.builder()
                .title(title)
                .url(url)
                .conferenceId(conferenceId)
                .objectKey(objectKey)
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

        return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, fileName);
    }

    @Override
    public List<GetFileDto> getFiles(FileReqDto fileReqDto) {
        List<Files> filesList = jpaFileRepository.findByConferenceIdAndCreatedAtBetween(fileReqDto.getConferenceId(), fileReqDto.getStart(), fileReqDto.getEnd());
        List<GetFileDto> dtoList = filesList.stream()
                .map(Files::of)
                .collect(Collectors.toList());
        return dtoList;
    }

    private String bucketName = "weffy-conference";
    @Override
    public ResponseEntity<byte[]> downloadFile(String objectKey, String filename) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            // S3에서 파일 내용 가져오기
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes());

            // 한글 파일 이름 인코딩 처리
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

            // HTTP 응답 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", encodedFilename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(objectBytes.asByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }

}

