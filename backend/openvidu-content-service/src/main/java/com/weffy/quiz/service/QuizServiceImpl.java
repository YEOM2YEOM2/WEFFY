package com.weffy.quiz.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service("QuizService")
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
}
