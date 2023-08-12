package openvidu.meeting.service.java.conference.streaming;

import io.openvidu.java.client.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.OpenviduDB;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
class SharedResource{
    public OpenVidu openvidu;
    public String classId;
    public String identification;
    public String recordingId; // 녹화한 파일 이름 Ex) SessionA, SessionA~1, SessionA~2
    public ZipFileDownloader zipFileDownloader; // 녹화한 url
    public int index; // 파일(classId)식별자  Ex) SessionA.mp4, SessionA1.mp4, SessionA2.mp4

    public boolean status;
    public boolean status1;
    public boolean status2;

    public List<String> urlList = new ArrayList<>();

    public SharedResource(String classId, String identification){
        openvidu = OpenviduDB.getOpenvidu();
        this.classId = classId;
        this.identification = identification;
        this.zipFileDownloader = new ZipFileDownloader(new RestTemplateBuilder());
        this.index = 0;
        this.status = true;
        this.status1 = true;
        this.status2 = true;
    }
    public void addUrl(String url){
        urlList.add(url);
    }
}

class StopThread extends Thread{
    @Override
    public void run(){
        try {
            Thread.sleep(20000); // 테스트 : 10초
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        while(sharedResource.status1){
            System.out.println("===================================StartAndStopRecording===================================");
            try{


                // 녹화 설정
                RecordingProperties properties = new RecordingProperties.Builder()
                        .outputMode(io.openvidu.java.client.Recording.OutputMode.INDIVIDUAL)
                        .hasAudio(true)
                        .hasVideo(true).build();

                Recording recording = sharedResource.openvidu.startRecording(sharedResource.classId, properties);
                sharedResource.setRecordingId(recording.getId()); // 녹화를 정지할 때 recordingId를 활용한다.


                if(!sharedResource.status){
                    System.out.println("thread-1 stop");
                    sharedResource.status1 = false;
                    break;
                }

                System.out.println("녹화를 시작합니다.");

                // 10분정지
                StopThread thread = new StopThread();
                thread.start();


                if(!sharedResource.status){
                    System.out.println("come-1");
                    thread.interrupt();
                    System.out.println("come-2");
                }else{
                    try {
                        thread.join(); // 현재 스레드를 thread가 끝날 때까지 기다림
                        System.out.println("come-4");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                // 녹화 정지
                System.out.println("녹화를 정지합니다.");
                recording = sharedResource.openvidu.stopRecording(sharedResource.recordingId);
                sharedResource.urlList.add(recording.getUrl());

                if(!sharedResource.status){
                    System.out.println("thread-2 stop");
                    sharedResource.status1 = false;
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
        while(sharedResource.status2){
            System.out.println("===================================FileDownload===================================");

            if(sharedResource.urlList.isEmpty() && !sharedResource.status && !sharedResource.status1){
                sharedResource.status2 = false;
                System.out.println("thread-2 stop");
            }

            if(!sharedResource.urlList.isEmpty()){



                String url = sharedResource.urlList.get(0);

                // url 설정
                sharedResource.zipFileDownloader.setZipFileUrl(url);

                // identification 설정
                sharedResource.setIdentification(sharedResource.identification);

                // identification 설정

                // 파일명 설정
                if(sharedResource.index == 0){
                    sharedResource.zipFileDownloader.setTitle(sharedResource.classId);
                }else{
                    sharedResource.zipFileDownloader.setTitle(sharedResource.classId+sharedResource.index);
                }

                sharedResource.index++;

                // recordingId 설정
                sharedResource.zipFileDownloader.setRecordingId(sharedResource.recordingId);

                // classId 설정
                sharedResource.zipFileDownloader.setClassId(sharedResource.classId);


                try {
                    if (sharedResource.zipFileDownloader.downloadRecording() != null) {
                        System.out.println("Download Success");
                    } else {
                        System.out.println("Download Fail");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                sharedResource.urlList.remove(0);
            }
        }

    }
}

// 메인 스레드
public class VideoRecorder {
    private SharedResource sharedResource;
    public Thread thread1, thread2;
    public VideoRecorder(String classId, String accessToken) {
        sharedResource = new SharedResource(classId, accessToken);
    }

    // Host가 녹화를 중지했을 때
    public void recordingStop(){
        System.out.println("정지");
        sharedResource.status = false;
        sharedResource.status1 = false;
        sharedResource.status2 = false;
    }


    // 처음 스레드를 정의하는 메소드
    public void recordingMethod() {
        thread1 = new Thread(new StartAndStopRecording(sharedResource));
        thread2 = new Thread(new FileDownload(sharedResource));

        thread1.start();
        thread2.start();

    }

}

