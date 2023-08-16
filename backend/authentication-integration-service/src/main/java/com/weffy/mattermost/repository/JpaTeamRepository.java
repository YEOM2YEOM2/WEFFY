package com.weffy.mattermost.repository;

import com.weffy.mattermost.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByIdentification(String identification);
}
