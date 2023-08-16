package openvidu.meeting.service.java.conference.streaming;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.Recording;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.OpenviduDB;
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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Setter
public class ZipFileDownloader{

    private Logger logger = LoggerFactory.getLogger(ZipFileDownloader.class);

    private final RestTemplate restTemplate;

    private OpenVidu openvidu;

    // @Value("${openvidu.api.username}")
    private String openViduApiUsername = "OPENVIDUAPP";

    //  @Value("${openvidu.api.password}")
    private String openViduApiPassword = "MY_SECRET";

    private String recordingFilePath = "C://recording/RecordingFile/";

    private String totalTextFile = "C://recording/TotalTextFile/";

    private String totalZipFilePath = "C://recording/TotalZipFile/";

    private String zipFileUrl;

    private String classId;

   // private String title; // classId + index

    private VideoCombine videoCombine;

    private String recordingId;

    private String identification;

    public void setZipFileSetting(String zipFileUrl,String classId, String recordingId, String identification){
        this.zipFileUrl = zipFileUrl;
        this.classId = classId;
     //   this.title = classId + index;
        this.recordingId = recordingId;
        this.identification = identification;
    }


    public ZipFileDownloader(RestTemplateBuilder restTemplateBuilder) {
        this.openvidu = OpenviduDB.getOpenvidu();
        this.restTemplate = restTemplateBuilder.build();
        this.videoCombine = new VideoCombine();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public String downloadRecording() throws IOException {

        System.out.println("다운로드 시작-!");

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

            Path pathName = Paths.get(totalZipFilePath).resolve(classId+".zip");

            Files.write(pathName, zipBytes);

            // 다운로드한 zip 파일을 압축 해제하고 내부의 파일을 처리한다.
            // unzipDir에 압축 해제된 파일들을 저장함
           // Path unzipDir = Files.createTempDirectory(Paths.get(totalZipFilePath),title);

            Path unzipDir = Files.createDirectories(Paths.get(totalZipFilePath).resolve(classId));
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


            // 녹화한 파일 삭제
            logger.info("녹화한 파일 삭제 : "+ recordingId);
            this.openvidu.deleteRecording(recordingId);
            logger.info("완료!");

            // 파일을 합친다.
            videoCombine.compressVideos(classId);

            //videoCombine.compressVideos("SessionA");


            // [폴더&파일 삭제]
             this.removeFolder(totalZipFilePath, classId, true); // C://recording/TotalZipFile/세션이름 폴더에 있는 모든 파일을 지운다 + 폴더도 지운다.

             //Files.deleteIfExists(Paths.get(totalZipFilePath+classId+".zip")); // zip 파일을 삭제한다.

             Files.deleteIfExists(Paths.get(totalTextFile+classId+".txt")); // C://recording/TotalTextFile/세션이름.txt 파일을 지운다.

            // S3 통신
//            try{
//                VideoSender sv = new VideoSender();
//                sv.sendRequest(classId, identification);
//                logger.info("sendRequest 호출");
//                //sv.sendRequest("SessionA", identification);
//                logger.info("Success : ");
//            }catch(Exception e){
//                e.printStackTrace();
//                logger.info("Error : ");
//            }


            return new StringBuilder(classId).append(".webm").toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void removeFolder(String deletePath, String classId, boolean ischeck){

        logger.info("removeFolder를 호출함");

        File folder = new File(deletePath+classId);

        if(folder.exists() && folder.isDirectory()){
            File[] files = folder.listFiles();
            if(files != null){
                for(File f : files){
                    f.delete();
                    logger.info(f.getName()+"삭제 완료");
                }
            }
        }
        // RecordingFile이 아닌 경우
        if(ischeck){
            folder.delete();
        }

    }




}
