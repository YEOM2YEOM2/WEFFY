package openvidu.meeting.service.java.conference.streaming;

import io.openvidu.java.client.*;
import openvidu.meeting.service.java.OpenviduDB;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.IOException;

public class Recording {
    private OpenVidu openvidu;

    public String classId;

    public String identification;
    public String recordingId;
    public ZipFileDownloader zipFileDownloader;

    public int index;

    public Recording(String classId, String identification){
        this.classId = classId;
        this.identification = identification;
        this.zipFileDownloader = new ZipFileDownloader(new RestTemplateBuilder());

        this.index = 0;
        openvidu = OpenviduDB.getOpenvidu();
    }

    public void myScheduledMethod() {
        this.startRecording();
        //zipFileDownloader = new ZipFileDownloader();
    }


    public void startRecording(){
        try{
            RecordingProperties properties = new RecordingProperties.Builder()
                    .outputMode(io.openvidu.java.client.Recording.OutputMode.INDIVIDUAL)
                    .hasAudio(true)
                    .hasVideo(true).build();

            io.openvidu.java.client.Recording recording = this.openvidu.startRecording(this.classId, properties);
            this.recordingId = recording.getId();
            System.out.println("녹음을 시작합니다.");

            // 10초 동안 녹화한다.
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.stopRecording();
        }catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace(); // 예외 정보를 출력하거나 다른 처리를 수행할 수 있습니다.
        }
    }

    public void stopRecording(){
        try{
            io.openvidu.java.client.Recording recording = this.openvidu.stopRecording(this.recordingId);

            System.out.println(recording.getUrl());

            zipFileDownloader.setZipFileUrl(recording.getUrl());

            if(index == 0){
                zipFileDownloader.setTitle(classId);
            }else{
                zipFileDownloader.setTitle(classId+(index++));
            }


            zipFileDownloader.setRecordingId(this.recordingId);

            String result = zipFileDownloader.downloadRecording();

            if (result != null) {
                System.out.println("ERROR : "+ result);
            } else {
                System.out.println("Recording download failed.");
            }

            this.startRecording();
        }catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
