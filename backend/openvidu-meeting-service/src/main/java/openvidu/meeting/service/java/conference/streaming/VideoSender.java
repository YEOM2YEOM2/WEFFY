package openvidu.meeting.service.java.conference.streaming;

import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.OpenviduDB;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class VideoSender {
    private Logger logger = LoggerFactory.getLogger(VideoSender.class);

    private String recordingFileUrl = "C:\\recording\\RecordingFile\\";
    private String accessToken;

    //"?type=lecture"
    public void sendRequest(String classId,  String identification) throws IOException {
        logger.info("sendRequest 호출! 호출! \n"+ accessToken);
        try{
            logger.info("sendRequest 호출!! 호출!! \n"+ accessToken);

            accessToken = OpenviduDB.getHostToken().get(identification);

            logger.info(classId+","+identification+"\n"+accessToken);

            HttpClient httpClient = HttpClients.createDefault();

            // "?type=lecture"
            HttpPost postRequest = new HttpPost("http://i9d107.p.ssafy.io:8081/api/v1/files/" + classId );

            postRequest.addHeader("Authorization", "Bearer " + accessToken);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            Path file = Paths.get(recordingFileUrl + classId + ".webm");

            logger.info(recordingFileUrl + classId + ".webm");

            // System.out.println("문장 : "+ file.toString());
            builder.addBinaryBody("file", Files.newInputStream(file), ContentType.APPLICATION_OCTET_STREAM, file.getFileName().toString());

            // System.out.println("file Path : "+ file.toString());

            HttpEntity multipartEntity = builder.build();
            postRequest.setEntity(multipartEntity);

            try (CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(postRequest)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("VideoSender ERROR-1");
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.info("VideoSender ERROR-2");
        }

    }
}
