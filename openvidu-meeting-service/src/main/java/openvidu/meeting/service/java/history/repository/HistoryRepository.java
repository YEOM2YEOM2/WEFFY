package openvidu.meeting.service.java.history.repository;

import openvidu.meeting.service.java.history.entity.Conference_History;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryRepository extends JpaRepository<Conference_History, Long>{
    Conference_History findTopByIdentificationAndConferenceIdOrderByIdDesc(String identification, Long conference_id);
}
