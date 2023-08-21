package openvidu.meeting.service.java.conference.streaming;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


// 파일을 합쳐서 저장하기


@Slf4j
public class VideoCombine {

    public Logger logger = LoggerFactory.getLogger(VideoCombine.class);

    public StringBuilder recordingFileUrl = new StringBuilder().append("C:\\recording\\RecordingFile\\");

    public StringBuilder textFileUrl = new StringBuilder().append("C:\\recording\\TotalTextFile\\");


    public void compressVideos(String classId) throws IOException, InterruptedException {

        textFileUrl.append(classId+".txt");

        String newRecordingFileUrl = new StringBuilder().append(recordingFileUrl.toString()).append(classId+".webm").toString();
        // 파일을 합친다.
        logger.info("VideoCombine Merge!!!");
        String command = "ffmpeg -f concat -safe 0 -i "+textFileUrl.toString()+" -c copy "+ newRecordingFileUrl;
        ProcessBuilder processBuilder1 = new ProcessBuilder(command.split(" "));
        processBuilder1.redirectErrorStream(true);
        Process process1 = processBuilder1.start();
        process1.waitFor();

        // 해상도 조절
        logger.info("VideoCombine Resolution!!!");
        String combineCommand = "ffmpeg -i "+newRecordingFileUrl+" -vf \"scale=1920:1080\" "+ newRecordingFileUrl;
        ProcessBuilder processBuilder2 = new ProcessBuilder(combineCommand.split(" "));
        processBuilder2.redirectErrorStream(true);
        Process process2 = processBuilder2.start();
        process2.waitFor();



//        String command;
//        boolean ischeck = false;
//        // recordingFileUrl.append(classId+".webm").toString()
//        if(!Files.exists(Paths.get(new StringBuilder().append(recordingFileUrl.toString()).append(classId+"-1.webm").toString()))){
//            command = "ffmpeg -f concat -safe 0 -i "+textFileUrl.toString()+" -c copy "+ new StringBuilder().append(recordingFileUrl.toString()).append(classId+"-1.webm");
//        }else{
//            ischeck = true;
//            logger.info("ischeck : "+ ischeck+" => "+ new StringBuilder().append(recordingFileUrl.toString()).append(classId+"-2.webm"));
//            command = "ffmpeg -f concat -safe 0 -i "+textFileUrl.toString()+" -c copy "+ new StringBuilder().append(recordingFileUrl.toString()).append(classId+"-2.webm");
//        }


//        if(ischeck){
//     //        폴더의 classId.webm 파일 삭제
//            File folder = new File(recordingFileUrl.toString());
//            if(folder.isDirectory()){
//                File[] files = folder.listFiles();
//                for(File file : files){
//                    if(file.getName().equals(classId+".webm")){
//                        logger.info("삭제 : "+ file.getName());
//                        file.delete();
//                    }
//                }
//            }
//
//            logger.info("wait!");
//
//            // classId1 의 이름을 변경한다.
//            logger.info("path1 : "+ new StringBuilder().append(recordingFileUrl.toString()).append(classId+"1.webm").toString());
//            logger.info("path2 : "+ new StringBuilder().append(recordingFileUrl.toString()).append(classId+".webm").toString());
//            File oldFile = new File(new StringBuilder().append(recordingFileUrl.toString()).append(classId+"1.webm").toString());
//            File newFile = new File(new StringBuilder().append(recordingFileUrl.toString()).append(classId+".webm").toString());
//
//            boolean isCheck = oldFile.renameTo(newFile);
//
//
//            logger.info("수정한 결과 : "+ isCheck);
//        }


        logger.info(classId + " 합치기 완료!!");

    }


}
