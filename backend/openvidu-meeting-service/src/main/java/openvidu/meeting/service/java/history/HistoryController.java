package openvidu.meeting.service.java.history;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openvidu.meeting.service.java.common.dto.BaseResponseBody;
import openvidu.meeting.service.java.history.dto.request.HistoryReqDto;
import openvidu.meeting.service.java.history.entity.Conference_History;
import openvidu.meeting.service.java.history.service.HistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference/history")
public class HistoryController {
    private final HistoryService historyService;

    //conference_history 생성
    @PostMapping("/create")
    public ResponseEntity<? extends BaseResponseBody>createHistory(@RequestBody HistoryReqDto dto, @RequestParam(name = "active", required = false) String active )throws IOException {
        historyService.createHistory(dto, active);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseBody.of(201, "CREATED"));
    }

    //특정 conference 방과 userid받아서 제일 최근꺼 반환
    @GetMapping("/detail")
    public ResponseEntity<? extends BaseResponseBody>detailHistory(@RequestBody HistoryReqDto dto){

        Conference_History history = historyService.detailHistory(dto);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, history));
    }



}
