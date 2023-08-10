package openvidu.meeting.service.java.conference.streaming;


import lombok.Setter;
import lombok.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Setter
public class ZipFileDownloader {

    private final RestTemplate restTemplate;

    private String openViduApiUsername = "OPENVIDUAPP";

    private String openViduApiPassword = "MY_SECRET";

    private String localRecordingPath = "C://recording/";

    private String zipFileUrl;

    private String title; // classId + index

    private String recordingId;

    public ZipFileDownloader(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public String downloadRecording() throws IOException {
        System.out.println("start");
        // Create Basic Auth header
        try{
            String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString((openViduApiUsername + ":" + openViduApiPassword).getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeaderValue);

            System.out.println(recordingId);

            // 녹화 파일을 GET 요청으로 다운로드한다.
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    zipFileUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    byte[].class
            );

            // 다운로드한 zip 파일을 임시 파일로 저장합니다. recordingId 변수를 파일 이름에 사용하며, Files.write를 사용하여 내용을 기록합니다.
            byte[] zipBytes = response.getBody();
            Path tempFile = Files.createTempFile(recordingId, ".zip");
            Files.write(tempFile, zipBytes);
            System.out.println(tempFile.toString());

            // 다운로드한 zip 파일을 압축 해제하고 내부의 파일을 처리합니다. unzipDir에 압축 해제된 파일들이 저장됩니다.
            Path unzipDir = Files.createTempDirectory(recordingId);
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(tempFile))) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    Path entryPath = unzipDir.resolve(entry.getName());
                    Files.copy(zipInputStream, entryPath, StandardCopyOption.REPLACE_EXISTING);
                    zipInputStream.closeEntry();
                    System.out.println("name : "+ entry.getName());
                    // Rename and move the extracted file
                    if (entry.getName().endsWith(".webm")) {
                        Path renamedMp4Path = unzipDir.resolve(title+".mp4"); // 바꿀 이름 설정
                        Files.move(entryPath, renamedMp4Path, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            System.out.println(unzipDir);

            // 압축 해제된 디렉토리에 있는 mp4 파일의 경로와 로컬 저장 경로를 설정합니다.
            // 이 부분에서 extractedMp4Path와 targetPath의 경로가 정확한지 확인하는 데 유용합니다.
            Path extractedMp4Path = unzipDir.resolve(recordingId+".mp4");
            Path targetPath = Paths.get(localRecordingPath, recordingId+".mp4");
            System.out.println(extractedMp4Path);
            System.out.println(targetPath);

            //Files.move(extractedMp4Path, targetPath, StandardCopyOption.REPLACE_EXISTING);

            //Files.delete(tempFile);
            //Files.delete(unzipDir);

            return "Recording downloaded and saved locally.";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }




}
