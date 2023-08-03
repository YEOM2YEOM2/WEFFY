package com.weffy.mattermost.service;

import com.weffy.mattermost.entity.Session;
import com.weffy.mattermost.repository.JpaSessionRepository;
import com.weffy.user.entity.WeffyUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MattermostService")
@RequiredArgsConstructor
public class MattermostServiceImpl implements MattermostService {
    @Autowired
    private JpaSessionRepository jpaSessionRepository;

    @Override
    @Transactional
    public void saveSession(WeffyUser weffyUser, String token) {
        Session session = Session.builder()
                .token(token)
                .weffyUser(weffyUser)
                .build();

        jpaSessionRepository.save(session);
    }
}
