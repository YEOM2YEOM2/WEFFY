package com.weffy.file.repository;

import com.weffy.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaFileRepository extends JpaRepository<Files, Long> {
    List<Files> findByConferenceIdAndCreatedAtBetween(String conferenceId, LocalDateTime start, LocalDateTime end);
}
