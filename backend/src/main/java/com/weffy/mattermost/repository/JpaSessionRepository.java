package com.weffy.mattermost.repository;

import com.weffy.mattermost.entity.Session;
import com.weffy.user.entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaSessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByWeffyUser(WeffyUser weffyUser);
}
