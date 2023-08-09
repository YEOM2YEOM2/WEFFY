//package openvidu.meeting.service.java.conference;
//
//import lombok.extern.slf4j.Slf4j;
//import openvidu.meeting.service.java.common.dto.BaseResponseBody;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/convert")
//@CrossOrigin(origins = "*")
//public class FFmpegController {
//
//    public List<String> getFileNamesInDirectory(){
//        String directoryPath = "C:/recording/";
//        File directory = new File(directoryPath);
//
//        List<String> fileNames = new ArrayList<>();
//
//        if(directory.isDirectory() && directory.exists()){
//            File[] files = directory.listFiles();
//            for(File file : files){
//                if(file.isFile()){
//                    System.out.println(file.getName());
//                    fileNames.add(file.getName());
//                }
//            }
//        }
//        return fileNames;
//    }
//
//    @GetMapping("/fileNames")
//    public ResponseEntity<? extends BaseResponseBody> fileNameList(){
//        List<String> names = getFileNamesInDirectory();
//        return null;
//    }
//}
