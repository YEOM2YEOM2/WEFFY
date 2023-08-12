package com.weffy.quiz.repository;

import com.weffy.quiz.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends JpaRepository<Answer, Long> {
}
