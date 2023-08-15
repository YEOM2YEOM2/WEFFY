package openvidu.meeting.service.java.conference;

import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.common.dto.BaseResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@RequestMapping("/stream")
@RestController
public class StreamController {

    private Logger logger = LoggerFactory.getLogger(StreamController.class);

    private StringBuilder totalZipFilePath = new StringBuilder().append("C:\\recording\\TotalZipFile\\");
    private StringBuilder totalTextFilePath = new StringBuilder().append("C:\\recording\\TotalTextFile\\");

    @PostMapping("/{class_id}/{stream_id}")
    public ResponseEntity<? extends BaseResponseBody>getStreamId(
            @PathVariable(name = "class_id")String classId,
            @PathVariable(name = "stream_id")String streamId) throws IOException {

        // streamId를 txt 파일에 저장한다.

        // 파일 경로 C:\recording\TotalTextFile\classId.txt
        String newFilePath = new StringBuilder().append(totalTextFilePath).append(classId).append(".txt").toString();

        Path streamFilePath = Paths.get(newFilePath);

        // 존재하지 않는 txt 파일이면 만든다.
        if(!Files.exists(streamFilePath)){
            Files.createFile(streamFilePath);
        }

        try{
            FileWriter writer = new FileWriter(newFilePath, true);
            writer.write(new StringBuilder().append("file \'").append(totalZipFilePath).append(classId).append("\\").append(streamId).append(".webm\'\n").toString());
            writer.close();
            logger.info("파일에 내용을 추가함");
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(202, "파일에 내용을 추가함"));
        }catch (Exception e){
            e.printStackTrace();
            logger.info("파일에 스트림 ID를 추가할 수 없음");
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(404, "파일에 스트림 ID를 추가할 수 없음"));
        }



    }
}
