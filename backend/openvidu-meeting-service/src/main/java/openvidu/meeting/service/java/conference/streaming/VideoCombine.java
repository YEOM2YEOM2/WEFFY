package openvidu.meeting.service.java.conference.streaming;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


// 파일을 합쳐서 저장하기


@Slf4j
public class VideoCombine {

    public Logger logger = LoggerFactory.getLogger(VideoCombine.class);

    public StringBuilder recordingFileUrl = new StringBuilder().append("C:\\recording\\RecordingFile\\");

    public StringBuilder textFileUrl = new StringBuilder().append("C:\\recording\\TotalTextFile\\");


    public void compressVideos(String classId) throws IOException, InterruptedException {

        recordingFileUrl.append(classId+".webm");

        textFileUrl.append(classId+".txt");

        // 파일을 합친다.
        String command = "ffmpeg -f concat -safe 0 -i "+textFileUrl.toString()+" -c copy "+recordingFileUrl.toString();
        ProcessBuilder processBuilder1 = new ProcessBuilder(command.split(" "));
        processBuilder1.redirectErrorStream(true);
        Process process1 = processBuilder1.start();
        process1.waitFor();


        // 해상도 조절
        String combineCommand = "ffmpeg -i "+recordingFileUrl.toString()+" -vf \"scale=1920:1080\" "+recordingFileUrl.toString();
        ProcessBuilder processBuilder2 = new ProcessBuilder(combineCommand.split(" "));
        processBuilder2.redirectErrorStream(true);
        Process process2 = processBuilder2.start();
        process2.waitFor();


        logger.info(classId + "합치기 완료!!");

    }


}
