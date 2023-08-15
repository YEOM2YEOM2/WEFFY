package openvidu.meeting.service.java.conference.streaming;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Setter
public class ZipFileDownloader{

    private Logger logger = LoggerFactory.getLogger(ZipFileDownloader.class);

    private final RestTemplate restTemplate;

    // @Value("${openvidu.api.username}")
    private String openViduApiUsername = "OPENVIDUAPP";

    //  @Value("${openvidu.api.password}")
    private String openViduApiPassword = "MY_SECRET";

    private String recordingFilePath = "C://recording/RecordingFile/";

    private String totalZipFilePath = "C://recording/TotalZipFile/";

    private String zipFileUrl;

    private String classId;

    private String title; // classId + index

    private VideoCombine videoCombine;

    private String recordingId;

    private String identification;


    public ZipFileDownloader(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.videoCombine = new VideoCombine();

       // this.identification = "jenny";
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public String downloadRecording() throws IOException {

        try{
            // Openvidu의 인증 절차
            // 이름과 비밀번호를 만들어서 http header에 넣어준다.
            String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString((openViduApiUsername + ":" + openViduApiPassword).getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeaderValue);

            // 녹화 파일을 GET 요청으로 다운로드한다.
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    zipFileUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    byte[].class
            );


            byte[] zipBytes = response.getBody();

            Path pathName = Paths.get(totalZipFilePath).resolve(title+".zip");

            Files.write(pathName, zipBytes);

            // 다운로드한 zip 파일을 압축 해제하고 내부의 파일을 처리한다.
            // unzipDir에 압축 해제된 파일들을 저장함
           // Path unzipDir = Files.createTempDirectory(Paths.get(totalZipFilePath),title);

            Path unzipDir = Files.createDirectories(Paths.get(totalZipFilePath).resolve(title));
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(pathName))) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {

                    Path entryPath = unzipDir.resolve(entry.getName());

                    if (entry.getName().startsWith("str_SCR") && entry.getName().endsWith(".webm")) {
                        Files.copy(zipInputStream, entryPath, StandardCopyOption.REPLACE_EXISTING);
                        zipInputStream.closeEntry();
                    }
                }
            }

            videoCombine.compressVideos(classId);



//            // 압축 해제된 디렉토리에 있는 mp4 파일의 경로와 로컬 저장 경로를 설정한다.
//            Path extractedMp4Path = unzipDir.resolve(title+".mp4");
//
//            Path targetPath = Paths.get(new StringBuilder().append(this.recordingFilePath).append(this.classId).append("/").toString(), title+".mp4");
//
//            // 기존에 저장한 폴더에서 지정한 로컬 경로로 파일을 옮긴다.
//            Files.move(extractedMp4Path, targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//           //  생성한 mp4 파일을 s3에 저장할 수 있도록 header에 담아서 보낸다.
//
//            try{
//                VideoSender sv = new VideoSender();
//                sv.sendRequest(classId, title, identification);
//                logger.info("Success : ");
//            }catch(Exception e){
//                e.printStackTrace();
//                logger.info("Error : ");
//            }
//
//            // 로컬에 다운받은 임시 파일을 지운다.
//            Files.delete(pathName);
//
//            // 압축을 폴었던 파일에 json 파일도 있었기 때문에 하위 파일을 먼저 지우고 폴더를 삭제한다.
//            Files.delete(Paths.get(unzipDir.toString()).resolve(recordingId+".json"));
//            Files.delete(unzipDir);

            return new StringBuilder(title).append(".mp4").toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void removeFolder(String classId){

        logger.info("removeFolder를 호출함");

        File folder = new File(recordingFilePath+classId);

        if(folder.exists() && folder.isDirectory()){
            File[] files = folder.listFiles();
            if(files != null){
                for(File f : files){
                    f.delete();
                    logger.info(f.getName()+"삭제 완료");
                }
            }
        }

        folder.delete();

    }




}
