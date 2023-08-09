package com.weffy.mattermost.repository;

import com.weffy.mattermost.entity.WeffyUserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserTeamRepository extends JpaRepository<WeffyUserTeam, Long> {
}
