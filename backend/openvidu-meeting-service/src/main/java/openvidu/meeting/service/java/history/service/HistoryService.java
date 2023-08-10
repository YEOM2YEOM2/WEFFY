package openvidu.meeting.service.java.history.service;


import openvidu.meeting.service.java.history.dto.request.HistoryReqDto;
import openvidu.meeting.service.java.history.entity.Conference_History;

import java.io.IOException;

public interface HistoryService {
    void createHistory(HistoryReqDto dto, String active) throws IOException;

    Conference_History detailHistory(HistoryReqDto dto);

}
