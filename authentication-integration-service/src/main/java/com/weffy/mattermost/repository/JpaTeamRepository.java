package com.weffy.mattermost.repository;

import com.weffy.mattermost.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByIdentification(String identification);
}
