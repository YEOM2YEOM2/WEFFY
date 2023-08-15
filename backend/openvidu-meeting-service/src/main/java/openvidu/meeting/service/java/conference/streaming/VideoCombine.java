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

    // inputVideo1 : "C://recording/RecordingFile/classId/TotalZipFile/classId"
    public void compressVideos(String classId) throws IOException, InterruptedException {

        //
        Path path1 = Paths.get("C://recording/TotalZipFile/"+classId);

        String recordingFileUrl = "C:\\recording\\TotalRecordingFile\\"+classId+".webm";

        String textFileUrl = "C:\\recording\\TotalTextFile\\"+classId+".txt";

        // String command = "ffmpeg -f concat -safe 0 -i C:\\recording\\mylist.txt -c copy C:\\recording\\nimo.webm";
        String command = "ffmpeg -f concat -safe 0 -i "+textFileUrl+" -c copy "+recordingFileUrl;
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();

    }


}
