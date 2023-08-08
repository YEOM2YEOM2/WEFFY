package io.openvidu.basic.java.history.repository;

import io.openvidu.basic.java.history.entity.Conference_History;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryRepository extends JpaRepository<Conference_History, Long>{
}
