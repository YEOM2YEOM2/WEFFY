package openvidu.meeting.service.java.conference.streaming;

import io.openvidu.java.client.*;
import lombok.Getter;
import lombok.Setter;
import openvidu.meeting.service.java.OpenviduDB;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.IOException;


@Getter
@Setter
public class MediaRecording {
    private OpenVidu openvidu;

    private String classId;

    private String identification;
    private String recordingId; // 녹화한 파일 이름 Ex) SessionA, SessionA~1, SessionA~2
    private ZipFileDownloader zipFileDownloader; // 녹화한 url
    private int index; // 파일(classId)식별자  Ex) SessionA.mp4, SessionA1.mp4, SessionA2.mp4


    private boolean status;

    public MediaRecording(String classId, String identification){
        openvidu = OpenviduDB.getOpenvidu();
        this.classId = classId;
        this.identification = identification;
        this.zipFileDownloader = new ZipFileDownloader(new RestTemplateBuilder());
        this.index = 0;
        this.status = true;
    }

    // 처음에 시작하는 메소드
    public void scheduledMethod() {
        this.startRecording();
    }


    private void startRecording(){
        try{
            // 녹화 설정
            RecordingProperties properties = new RecordingProperties.Builder()
                    .outputMode(io.openvidu.java.client.Recording.OutputMode.INDIVIDUAL)
                    .hasAudio(true)
                    .hasVideo(true).build();

            Recording recording = openvidu.startRecording(this.classId, properties);
            setRecordingId(recording.getId()); // 녹화를 정지할 때 recordingId를 활용한다.

            System.out.println("녹화를 시작합니다.");

            // 10초 동안 녹화한다.
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(status){
                // 녹화 정지
                this.stopRecording();
            }
        }catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace(); // 예외 정보를 출력하거나 다른 처리를 수행할 수 있습니다.
        }
    }

    private void stopRecording(){
        try{
            Recording recording = openvidu.stopRecording(this.recordingId);

            System.out.println("녹화 url : "+recording.getUrl());

            // url 설정
            zipFileDownloader.setZipFileUrl(recording.getUrl());

            // 파일명 설정
            if(index == 0){
                zipFileDownloader.setTitle(classId);
            }else{
                zipFileDownloader.setTitle(classId+index);
            }

            index++;

            // recordingId 설정
            zipFileDownloader.setRecordingId(this.recordingId);

            // classId 설정
            zipFileDownloader.setClassId(this.classId);


            if (zipFileDownloader.downloadRecording() != null) {
                System.out.println("Download Success");
            } else {
                System.out.println("Download Fail");
            }

            if(status){
                this.startRecording();
            }
        }catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
