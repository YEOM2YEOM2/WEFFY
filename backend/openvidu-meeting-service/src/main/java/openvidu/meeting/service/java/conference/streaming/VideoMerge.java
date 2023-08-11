package openvidu.meeting.service.java.conference.streaming;

import lombok.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VideoMerge {
//
//    private String ffmpegPath = "C:\\ffmpeg\\bin\\ffmpeg.exe";
//
//    //private String ffprobePath = "/ffmpeg/bin/ffprobe.exe";
//    public void mergeVideos(String input1Path, String input2Path, String outputPath) {
//        try {
//            System.out.println("come-1");
//            // FFmpeg 명령어 생성
//            String[] cmd = {
//                    ffmpegPath,
//                    "-i", input1Path,
//                    "-i", input2Path,
//                    "-filter_complex", "[0:v] [0:a] [1:v] [1:a] concat=n=2:v=1:a=1 [v] [a]",
//                    "-map", "[v]",
//                    "-map", "[a]",
//                    outputPath
//            };
//            System.out.println("come-2");
//            // FFmpeg 실행
//            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
//            Process process = processBuilder.start();
//
//            System.out.println(process.getErrorStream());
//
//            System.out.println("come-3");
//
//            // 프로세스의 입력 및 출력 스트림 비우기
//            new Thread(() -> {
//                try {
//                    InputStream inputStream = process.getInputStream();
//                    byte[] buffer = new byte[1024];
//                    while (inputStream.read(buffer) != -1) {
//                        // 아무 작업 없이 버퍼를 비움
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//            // FFmpeg 실행 결과 처리 (선택사항)
//            int exitCode = process.waitFor();
//
//            System.out.println(process.getErrorStream());
//
//            // 오류 스트림 읽기
//            InputStream errorStream = process.getErrorStream();
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
//            String line;
//            while ((line = errorReader.readLine()) != null) {
//                System.err.println(line); // 오류 메시지 출력
//            }
//
//            System.out.println("come-4");
//            if (exitCode == 0) {
//                System.out.println("동영상 합치기 성공");
//            } else {
//                System.out.println("동영상 합치기 실패");
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
