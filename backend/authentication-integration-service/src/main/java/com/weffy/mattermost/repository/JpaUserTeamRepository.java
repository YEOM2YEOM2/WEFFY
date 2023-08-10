package com.weffy.mattermost.repository;

import com.weffy.mattermost.entity.Team;
import com.weffy.mattermost.entity.WeffyUserTeam;
import com.weffy.user.entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserTeamRepository extends JpaRepository<WeffyUserTeam, Long> {
        Optional<WeffyUserTeam> findByTeamAndWeffyUser(Team team, WeffyUser weffyUser);

        List<WeffyUserTeam> findByWeffyUser(WeffyUser weffyUser);
}
