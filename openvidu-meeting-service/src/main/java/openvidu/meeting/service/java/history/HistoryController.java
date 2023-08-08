package openvidu.meeting.service.java.history;

import openvidu.meeting.service.java.history.repository.HistoryRepository;
import openvidu.meeting.service.java.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conference/history")
public class HistoryController {
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;

//    @PostMapping("/create")
//    public ResponseEntity<>

}
