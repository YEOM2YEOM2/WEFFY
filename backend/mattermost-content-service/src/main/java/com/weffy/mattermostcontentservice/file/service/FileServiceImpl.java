package com.weffy.mattermostcontentservice.file.service;

import com.weffy.mattermostcontentservice.file.dto.FileDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("fileService")
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final Logger logger = (Logger) LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${mattermost_url}")
    private String MATTERMOST_URL;

    @Value("${max_batch_size}")
    private int MAX_BATCH_SIZE;

    @Value("${max_file_size}")
    private long maxFileSize= 52428800;

    @Override
    public void uploadFilesMM(String sessionToken, String classId, List<FileDto> lists) throws IOException, URISyntaxException {
        List<MultipartFile> multipartFiles = new ArrayList<>();

        for (FileDto file : lists) {
            MultipartFile multipartFile = s3UrlToMultipartFile(file.getFileUrl(),file.getFileName());
            multipartFiles.add(multipartFile);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(sessionToken);

        //최대 MAX_BATCH_SIZE 개씩 묶어서 전송한다.
        for(int i=0; i<multipartFiles.size(); i+=MAX_BATCH_SIZE){
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            List<String> fileIds = new ArrayList<>();

            //묶음 처리
            for(int j=i; j < i + MAX_BATCH_SIZE && j < multipartFiles.size(); j++){
                MultipartFile file = multipartFiles.get(j);

                //파일크기제한
                if(file.getSize()>=maxFileSize){
                    continue;
                }
                body.add("files", new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                });

                body.add("channel_id", classId);
                ResponseEntity<String> uploadResponse = restTemplate.postForEntity(MATTERMOST_URL + "/files", new HttpEntity<>(body, headers), String.class);

                body.clear();

                JSONObject jsonResponse = new JSONObject(uploadResponse.getBody());
                String fileId = jsonResponse.getJSONArray("file_infos").getJSONObject(0).getString("id");
                fileIds.add(fileId);
            }

            // 파일 ID 리스트로 Post 요청
            JSONObject postBody = new JSONObject();
            postBody.put("channel_id", classId);
            postBody.put("message", "");
            postBody.put("file_ids", new JSONArray(fileIds));

            logger.info(postBody.toString());

            restTemplate.postForEntity(MATTERMOST_URL + "/posts", new HttpEntity<>(postBody.toString(), headers), String.class);

        }

    }

    public MultipartFile s3UrlToMultipartFile(String s3Url, String originalFileName) throws IOException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(new URL(s3Url).toURI(), byte[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IOException("s3에서 파일 다운로드 실패 " + response.getStatusCode());
        }

        byte[] fileBytes = response.getBody();

        MultipartFile multipartFile = new ByteArrayMultipartFile(fileBytes, "file", originalFileName, "application/octet-stream");

        return multipartFile;
    }

    public static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] fileContent;
        private final String fieldName;
        private final String originalFilename;
        private final String contentType;

        public ByteArrayMultipartFile(byte[] fileContent, String fieldName, String originalFilename, String contentType) {
            this.fileContent = fileContent;
            this.fieldName = fieldName;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return fieldName;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return fileContent == null || fileContent.length == 0;
        }

        @Override
        public long getSize() {
            return fileContent.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return fileContent;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(fileContent);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            new FileOutputStream(dest).write(fileContent);
        }
    }
}
