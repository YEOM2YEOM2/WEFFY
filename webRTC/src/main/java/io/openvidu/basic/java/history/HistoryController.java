package io.openvidu.basic.java.history;

import io.openvidu.basic.java.history.repository.HistoryRepository;
import io.openvidu.basic.java.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
