package openvidu.meeting.service.java.history.service;


import lombok.RequiredArgsConstructor;
import openvidu.meeting.service.java.history.dto.request.HistoryReqDto;
import openvidu.meeting.service.java.history.entity.Active;
import openvidu.meeting.service.java.history.entity.Conference_History;
import openvidu.meeting.service.java.history.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

@Transactional
@Service("historyService")
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository;

    @Override
    public void createHistory(HistoryReqDto dto, String active) throws IOException{
        Active type = null;
        Date now = new Date();
        if(active.equals("CREATE")){
            type = Active.CREATE;
        }
        else if(active.equals("CONNECTION")){
            type = Active.CONNECTION;
        }
        else if(active.equals("EXIT")){
            type = Active.EXIT;
        }
        else if(active.equals("LEAVE")){
            type = Active.LEAVE;
        }
        else if (active.equals("DELTE")){
            type = Active.DELETE;
        }
        historyRepository.save(
            Conference_History.builder()
                    .identification(dto.getIdentification())
                    .conference_id(dto.getConference_id())
                    .active(type)
                    .insert_time(now)
                    .build()
        );
    }

    @Override
    public Conference_History detailHistory(HistoryReqDto reqDto){
        return historyRepository.findTopByIdentificationAndConferenceIdOrderByIdDesc(reqDto.getIdentification(), reqDto.getConference_id());
    }

}
