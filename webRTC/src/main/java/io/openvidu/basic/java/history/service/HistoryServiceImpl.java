package io.openvidu.basic.java.history.service;

import io.openvidu.basic.java.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("historyService")
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository;


}
