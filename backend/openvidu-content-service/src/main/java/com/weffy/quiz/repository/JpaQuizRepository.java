package com.weffy.quiz.repository;

import com.weffy.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaQuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByConferenceId(String conferenceId);
}
