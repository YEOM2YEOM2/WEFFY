package openvidu.meeting.service.java.conference;

import openvidu.meeting.service.java.OpenviduDB;
import openvidu.meeting.service.java.common.dto.BaseResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/communication")
public class CommunicationController {

    // connectionId를 얻기 위한 API
    @PostMapping("/{class_id}/{identification}")
    public ResponseEntity<String>getConnectionId(
        @PathVariable(name="class_id") String classId,
        @PathVariable(name="identification") String identification
    ){
        try{
            String connectionId = OpenviduDB.getSessionConnectionList().get(classId).get(identification);
            return new ResponseEntity<>(connectionId, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("404 ERROR", HttpStatus.NOT_FOUND);
        }
    }
}
