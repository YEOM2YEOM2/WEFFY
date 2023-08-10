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
        WeffyUser weffyUser = userRepository.findByIdentification(identification)
                .orElseThrow(() ->  new CustomException(ExceptionEnum.USERNOTEXIST));

        // 팀 정보 조회
        JsonNode teamResult = mattermostHandler.getTeam(identification, sessionToken);
        for (JsonNode cur : teamResult) {
            Team team;
            String ident = cur.get("id").asText();
            // 팀 정보가 존재하지 않으면 저장
            Optional<Team> teamInfo = jpaTeamRepository.findByIdentification(ident);
            if (teamInfo.isEmpty()) {
                team = Team.builder()
                        .identification(ident)
                        .name(cur.get("display_name").asText())
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

            // 채널 정보를 조회
            JsonNode channelResult = mattermostHandler.getChannel(identification, ident, sessionToken);
            for (JsonNode channelCur : channelResult) {

                String type = channelCur.get("type").asText();
                if (type.equals("O") || type.equals("P")) {
                    String channelId = channelCur.get("id").asText();
                    String channelName = channelCur.get("display_name").asText();
                    Channel channel;
                    // 채널 정보가 존재하지 않으면 저장
                    Optional<Channel> channelInfo = jpaChannelRepository.findByIdentification(channelId);
                    if (channelInfo.isEmpty()) {
                        channel = Channel.builder()
                                .identification(channelId)
                                .name(channelName)
                                .type(type)
                                .team(team)
                                .build();

                        jpaChannelRepository.save(channel);
                    } else {
                        channel = channelInfo.get();
                    }
                    // 해당 채널에 user가 존재하지 않으면 저장
                    WeffyUserChannel weffyUserChannel;
                    Optional<WeffyUserChannel> weffyUserChannelInfo = jpaUserChannelRepository.findByChannelAndWeffyUser(channel, weffyUser);
                    Role role = mattermostHandler.getChannelRole(identification, channelId, sessionToken);

                    if (weffyUserChannelInfo.isEmpty()) {
                        weffyUserChannel = WeffyUserChannel.builder()
                                .channel(channel)
                                .weffyUser(weffyUser)
                                .role(role)
                                .build();
                    } else {
                        weffyUserChannel = weffyUserChannelInfo.get();
                        if (!weffyUserChannel.getRole().equals(role)){
                            weffyUserChannel.setRole(role);
                        }
                    }
                    jpaUserChannelRepository.save(weffyUserChannel);
                }
            }
        }
    }
}
