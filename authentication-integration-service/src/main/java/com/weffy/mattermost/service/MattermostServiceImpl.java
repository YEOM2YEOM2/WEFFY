package com.weffy.mattermost.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.mattermost.MattermostHandler;
import com.weffy.mattermost.entity.*;
import com.weffy.mattermost.repository.*;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service("MattermostService")
@RequiredArgsConstructor
public class MattermostServiceImpl implements MattermostService {
    private final JpaSessionRepository jpaSessionRepository;
    private final UserRepository userRepository;
    private final JpaTeamRepository jpaTeamRepository;
    private final JpaUserTeamRepository jpaUserTeamRepository;
    private final JpaChannelRepository jpaChannelRepository;
    private final JpaUserChannelRepository jpaUserChannelRepository;
    private final MattermostHandler mattermostHandler;

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
    public void saveTeam(String identification, String sessionToken) throws IOException, InterruptedException {
        WeffyUser weffyUser = findWeffyUserByIdentification(identification);
        JsonNode teamResult = mattermostHandler.getTeam(identification, sessionToken);

        for (JsonNode cur : teamResult) {
            Team team = handleTeamData(cur);
            handleUserTeamData(weffyUser, team);
            handleChannelData(identification, sessionToken, weffyUser, team, cur);
        }
    }

    private WeffyUser findWeffyUserByIdentification(String identification) {
        return userRepository.findByIdentification(identification)
                .orElseThrow(() ->  new CustomException(ExceptionEnum.USERNOTEXIST));
    }

    private Team handleTeamData(JsonNode cur) {
        String ident = cur.get("id").asText();
        return jpaTeamRepository.findByIdentification(ident).orElseGet(() -> {
            Team team = Team.builder()
                    .identification(ident)
                    .name(cur.get("display_name").asText())
                    .build();
            return jpaTeamRepository.save(team);
        });
    }

    private void handleUserTeamData(WeffyUser weffyUser, Team team) {
        jpaUserTeamRepository.findByTeamAndWeffyUser(team, weffyUser).orElseGet(() -> {
            return jpaUserTeamRepository.save(WeffyUserTeam.builder()
                    .team(team)
                    .weffyUser(weffyUser)
                    .build());
        });
    }

    private void handleChannelData(String identification, String sessionToken, WeffyUser weffyUser, Team team, JsonNode cur) throws IOException, InterruptedException {
        JsonNode channelResult = mattermostHandler.getChannel(identification, team.getIdentification(), sessionToken);
        for (JsonNode channelCur : channelResult) {
            String type = channelCur.get("type").asText();
            if (type.equals("O") || type.equals("P")) {
                Channel channel = handleChannelInfo(channelCur, team);
                handleUserChannelData(identification, sessionToken, weffyUser, channel);
            }
        }
    }

    private Channel handleChannelInfo(JsonNode channelCur, Team team) {
        String channelId = channelCur.get("id").asText();
        return jpaChannelRepository.findByIdentification(channelId).orElseGet(() -> {
            Channel channel = Channel.builder()
                    .identification(channelId)
                    .name(channelCur.get("display_name").asText())
                    .type(channelCur.get("type").asText())
                    .team(team)
                    .build();
            return jpaChannelRepository.save(channel);
        });
    }

    private void handleUserChannelData(String identification, String sessionToken, WeffyUser weffyUser, Channel channel) throws IOException, InterruptedException {
        Role role = mattermostHandler.getChannelRole(identification, channel.getIdentification(), sessionToken);
        jpaUserChannelRepository.findByChannelAndWeffyUser(channel, weffyUser)
                .ifPresentOrElse(weffyUserChannel -> {
                    if (!weffyUserChannel.getRole().equals(role)){
                        weffyUserChannel.setRole(role);
                        jpaUserChannelRepository.save(weffyUserChannel);
                    }
                }, () -> {
                    WeffyUserChannel weffyUserChannel = WeffyUserChannel.builder()
                            .channel(channel)
                            .weffyUser(weffyUser)
                            .role(role)
                            .build();
                    jpaUserChannelRepository.save(weffyUserChannel);
                });
    }

}
