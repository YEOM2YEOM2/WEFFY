package openvidu.meeting.service.java.conference.streaming;

import io.openvidu.java.client.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.OpenviduDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Getter
@Setter
class SharedResource{
    private OpenVidu openvidu;
    private String classId;
    private String identification;
    private String recordingId; // 녹화한 파일 이름 Ex) SessionA, SessionA~1, SessionA~2
    private ZipFileDownloader zipFileDownloader; // 녹화한 url
    private int index; // 파일(classId)식별자  Ex) SessionA.mp4, SessionA1.mp4, SessionA2.mp4

    private boolean status;
    private boolean status1;


    private boolean recordingState;

    private Thread thread2;

    private List<String> urlList = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(VideoRecorder.class);

    public SharedResource(String classId, String identification){
        this.openvidu = OpenviduDB.getOpenvidu();
        this.classId = classId;
        this.identification = identification;
        this.zipFileDownloader = new ZipFileDownloader(new RestTemplateBuilder());
        this.index = 0;
        this.status = true;
        this.status1 = true;
        this.recordingState = false;

    }
    public void addUrl(String url){
        urlList.add(url);
    }

    public void plusIndex(){
        this.index++;
    }
}

class StopThread extends Thread{
    private SharedResource sharedResource;

    public StopThread(SharedResource sharedResource){
        this.sharedResource = sharedResource;
    }
    @Override
    public void run(){
        try {
            while(sharedResource.isStatus()){
                if(Thread.interrupted() || !sharedResource.isStatus()){
                    return;
                }
                Thread.sleep(300000); // 테스트 : 5분 300000
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }
}


@Slf4j
// 녹화 시작&정지 스레드(thread1)
class StartAndStopRecording extends Thread{
    private SharedResource sharedResource;
    public StartAndStopRecording(SharedResource sharedResource){
        this.sharedResource = sharedResource;
    }

    @Override
    public void run(){
        while(sharedResource.isStatus1()){
            System.out.println("===================================StartAndStopRecording===================================");
            try{
                // 녹화 설정
                RecordingProperties properties = new RecordingProperties.Builder()
                        .resolution("1280x720")
                        .outputMode(io.openvidu.java.client.Recording.OutputMode.INDIVIDUAL)
                        .hasAudio(true)
                        .hasVideo(true).build();

                if(!sharedResource.isStatus()){
                    sharedResource.getLogger().info("thread-1 stop");
                    sharedResource.setStatus1(false);
                    break;
                }

                Recording recording = sharedResource.getOpenvidu().startRecording(sharedResource.getClassId(), properties);
                sharedResource.setRecordingState(true);
                sharedResource.setRecordingId(recording.getId()); // 녹화를 정지할 때 recordingId를 활용한다.

//                public FFmpegTest ffmpeg = new FFmpegTest();
//                public void ffmpegTest() throws IOException, InterruptedException {
//                    Path path1 = Paths.get("C:\\recording\\SCR1.webm");
//                    Path path2 = Paths.get("C:\\recording\\SCR2.webm");
//                    Path path3 = Paths.get("C:\\recording\\output.webm");
//
//                    ffmpeg.compressVideos(path1, path2, path3);
//                }

                sharedResource.getLogger().info(sharedResource.getRecordingId()+ "녹화를 시작합니다.");

                // 10분정지
                StopThread thread = new StopThread(sharedResource);
                thread.start();


                if(!sharedResource.isStatus()){
                    sharedResource.getLogger().info("come-1");
                    thread.interrupt();
                    sharedResource.getLogger().info("come-2");
                    break;
                }else{
                    try {
                        sharedResource.getLogger().info("come-3");
                        thread.join(); // 현재 스레드를 thread가 끝날 때까지 기다림
                    } catch (InterruptedException e) {
                        thread.interrupt();
                        e.printStackTrace();
                    }
                }


                // 녹화 정지
                sharedResource.getLogger().info("녹화를 정지합니다.");
                recording = sharedResource.getOpenvidu().stopRecording(sharedResource.getRecordingId());
                sharedResource.setRecordingState(false);

                sharedResource.getLogger().info("URL1 : "+recording.getUrl());

                sharedResource.getLogger().info("STATUE : "+ sharedResource.getThread2().getState());

                // 파일 다운로드 스레드 시작

                sharedResource.getUrlList().add(recording.getUrl());
                if(sharedResource.getThread2().getState() != State.RUNNABLE){
                    sharedResource.setThread2(new Thread(new FileDownload(sharedResource)));
                    sharedResource.getThread2().start();
                }


                if(!sharedResource.isStatus()){
                    sharedResource.getLogger().info("thread-2 stop");
                    sharedResource.setStatus1(false);
                }

            }catch (OpenViduJavaClientException | OpenViduHttpException e) {
                e.printStackTrace(); // 예외 정보를 출력하거나 다른 처리를 수행할 수 있습니다.
            }
        }

    }
}

// 파일 다운로드 스레드(thread2)
class FileDownload extends Thread{

    private SharedResource sharedResource;


    public FileDownload(SharedResource sharedResource){
        this.sharedResource = sharedResource;
    }

    @Override
    public void run(){
        sharedResource.getLogger().info("====================FileDownload===================");
        while(!sharedResource.getUrlList().isEmpty()){
            String url = sharedResource.getUrlList().get(0);

            sharedResource.getLogger().info("URL2 : "+ url);

            // url 설정
            sharedResource.getZipFileDownloader().setZipFileUrl(url);

            // identification 설정
            sharedResource.setIdentification(sharedResource.getIdentification());


            sharedResource.getZipFileDownloader().setIdentification(sharedResource.getIdentification());
            // 파일명 설정
            if(sharedResource.getIndex() == 0){
                sharedResource.getZipFileDownloader().setTitle(sharedResource.getClassId());
            }else{
                sharedResource.getZipFileDownloader().setTitle(sharedResource.getClassId()+sharedResource.getIndex());
            }

            sharedResource.plusIndex();

            // recordingId 설정
            sharedResource.getZipFileDownloader().setRecordingId(sharedResource.getRecordingId());

            // classId 설정
            sharedResource.getZipFileDownloader().setClassId(sharedResource.getClassId());

            try {
                if (sharedResource.getZipFileDownloader().downloadRecording() != null) {
                    sharedResource.getLogger().info("Download Success");
                } else {
                    sharedResource.getLogger().info("Download fail");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            sharedResource.getUrlList().remove(0);
        }


    }
}


@Slf4j
// 메인 스레드
public class VideoRecorder {
    private SharedResource sharedResource;
    public Thread thread1;
    public VideoRecorder(String classId, String accessToken) {
        sharedResource = new SharedResource(classId, accessToken);
    }

    // Host가 녹화를 중지했을 때
    public void recordingStop(){
        sharedResource.getLogger().info("Host가 녹화 정지버튼을 누른 경우");
        thread1.interrupt();
        sharedResource.setStatus(false);
    }


    // 처음 스레드를 정의하는 메소드
    public void recordingMethod() {
        thread1 = new Thread(new StartAndStopRecording(sharedResource));
        sharedResource.setThread2(new Thread(new FileDownload(sharedResource)));

        thread1.start();
    }

}