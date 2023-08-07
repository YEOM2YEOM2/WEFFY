package io.openvidu.basic.java.conference.repository;

import io.openvidu.basic.java.conference.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    // DB에서 전체 세션 다 가져오기(owner_id에 해당하는)
    List<Conference> findAllByOwnerId(String ownerId);

    // 해당 세션이 존재하는지 & DB에서 하나의 특정 세션 한 개만 가져오기
    Conference findByClassId(String classId);



}
