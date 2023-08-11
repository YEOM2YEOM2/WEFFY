package com.weffy.question.Repository;

import com.weffy.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaQuestionRepository extends JpaRepository<Question, Long> {

}
