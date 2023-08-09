package com.weffy.mattermost.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.mattermost.entity.Session;
import com.weffy.mattermost.entity.Team;
import com.weffy.mattermost.entity.WeffyUserTeam;
import com.weffy.mattermost.repository.JpaSessionRepository;
import com.weffy.mattermost.repository.JpaTeamRepository;
import com.weffy.mattermost.repository.JpaUserTeamRepository;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("MattermostService")
@RequiredArgsConstructor
public class MattermostServiceImpl implements MattermostService {
    private final JpaSessionRepository jpaSessionRepository;
    private final UserRepository userRepository;
    private final JpaTeamRepository jpaTeamRepository;
    private final JpaUserTeamRepository jpaUserTeamRepository;

    @Override
    @Transactional
    public void saveSession(WeffyUser weffyUser, String token) {
        Optional<Session> sessionToken = jpaSessionRepository.findByWeffyUser(weffyUser);
        if (sessionToken.isPresent()) sessionToken.get().setToken(token);
        else {
            Session session = Session.builder()
                    .token(token)
                    .weffyUser(weffyUser)
                    .build();

            jpaSessionRepository.save(session);
        }
    }

    @Override
    @Transactional
    public void saveTeam(String identification, JsonNode result) {
        WeffyUser weffyUser = userRepository.findByIdentification(identification)
                .orElseThrow(() ->  new CustomException(ExceptionEnum.USERNOTEXIST));
        for (JsonNode cur : result) {
            Team team;
            String ident = String.valueOf(cur.get("id"));
            // 팀 정보가 존재하지 않으면 저장
            Optional<Team> teamInfo = jpaTeamRepository.findByIdentification(ident);
            if (teamInfo.isEmpty()) {
                team = Team.builder()
                        .identification(ident)
                        .name(String.valueOf(cur.get("display_name")))
                        .build();
                jpaTeamRepository.save(team);
            } else {
                team = teamInfo.get();
            }
            // 해당 팀에 user가 존재하지 않으면 저장
            Optional<WeffyUserTeam> weffyUserTeam = jpaUserTeamRepository.findByTeamAndWeffyUser(team, weffyUser);
            if (weffyUserTeam.isEmpty()) {
                jpaUserTeamRepository.save(WeffyUserTeam.builder()
                        .team(team)
                        .weffyUser(weffyUser)
                        .build());
            }
        }
    }
}
