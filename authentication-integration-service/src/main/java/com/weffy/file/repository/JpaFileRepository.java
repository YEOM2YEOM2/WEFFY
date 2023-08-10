package com.weffy.file.repository;

import com.weffy.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFileRepository extends JpaRepository<Files, Long> {
}
