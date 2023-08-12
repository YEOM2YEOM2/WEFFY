package com.weffy.question.repository;

import com.weffy.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaQuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByConferenceId(String conferenceId);
}
