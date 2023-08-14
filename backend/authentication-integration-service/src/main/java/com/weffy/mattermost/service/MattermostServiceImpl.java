package com.weffy.mattermost.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.mattermost.MattermostHandler;
import com.weffy.mattermost.dto.response.TeamChannelResDto;
import com.weffy.mattermost.entity.*;
import com.weffy.mattermost.repository.*;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("mattermostService")
@Transactional
public class MattermostServiceImpl implements MattermostService {
    private final JpaSessionRepository jpaSessionRepository;
    private final UserRepository userRepository;
    private final JpaTeamRepository jpaTeamRepository;
    private final JpaUserTeamRepository jpaUserTeamRepository;
    private final JpaChannelRepository jpaChannelRepository;
    private final JpaUserChannelRepository jpaUserChannelRepository;
    private final MattermostHandler mattermostHandler;

    // 생성자를 직접 작성
    public MattermostServiceImpl(JpaSessionRepository jpaSessionRepository,
                                 UserRepository userRepository,
                                 JpaTeamRepository jpaTeamRepository,
                                 JpaUserTeamRepository jpaUserTeamRepository,
                                 JpaChannelRepository jpaChannelRepository,
                                 JpaUserChannelRepository jpaUserChannelRepository,
                                 MattermostHandler mattermostHandler) {
        this.jpaSessionRepository = jpaSessionRepository;
        this.userRepository = userRepository;
        this.jpaTeamRepository = jpaTeamRepository;
        this.jpaUserTeamRepository = jpaUserTeamRepository;
        this.jpaChannelRepository = jpaChannelRepository;
        this.jpaUserChannelRepository = jpaUserChannelRepository;
        this.mattermostHandler = mattermostHandler;
    }

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
                .orElseThrow(() ->  new CustomException(ExceptionEnum.USER_NOT_EXIST));
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


    @Override
    public List<TeamChannelResDto> getTeamAndChannel(WeffyUser weffyUser) {
        List<TeamChannelResDto> teamChannelResDtoList = new ArrayList<>();
        // 유저와 관련된 모든 팀 찾기
        List<WeffyUserTeam> userTeams = jpaUserTeamRepository.findByWeffyUser(weffyUser);

        for (WeffyUserTeam userTeam : userTeams) {
            TeamChannelResDto teamChannelResDto = new TeamChannelResDto();
            Team team = userTeam.getTeam();

            // 각 팀에 해당하는 모든 채널 찾기
            List<WeffyUserChannel> userChannels = jpaUserChannelRepository.findByWeffyUserAndChannel_Team(weffyUser, team);

            List<TeamChannelResDto.ChannelDto> channelDtos = userChannels.stream()
                    .map(userChannel -> {
                        TeamChannelResDto.ChannelDto channelDto = new TeamChannelResDto.ChannelDto();
                        Channel channel = userChannel.getChannel();
                        channelDto.setId(channel.getId());
                        channelDto.setIdentification(channel.getIdentification());
                        channelDto.setName(channel.getName());
                        channelDto.setAdmin(userChannel.getRole().equals(Role.channel_admin));
                        return channelDto;
                    })
                    .collect(Collectors.toList());

            teamChannelResDto.setId(team.getId());
            teamChannelResDto.setIdentification(team.getIdentification());
            teamChannelResDto.setName(team.getName());
            teamChannelResDto.setChannels(channelDtos);
            teamChannelResDtoList.add(teamChannelResDto);
        }
        return teamChannelResDtoList;
    }

    @Override
    @Transactional
    public int makeHeaderLink(WeffyUser weffyUser, String channelId) throws IOException, InterruptedException {
        Channel channel = findById(channelId);
        if (!weffyUser.getRole().equals(com.weffy.user.entity.Role.USER) || findByChannelAndWeffyUser(channel, weffyUser).equals(Role.channel_admin)) {
            String sessionToken = findByWeffyUser(weffyUser);
            return mattermostHandler.putHeaderLink(channelId, sessionToken);
        } else {
            throw new CustomException(ExceptionEnum.CANNOT_CREATE_ROOM);
        }
    }

    private Role findByChannelAndWeffyUser(Channel channel, WeffyUser weffyUser) {
        return jpaUserChannelRepository.findByChannelAndWeffyUser(channel, weffyUser)
                .map(WeffyUserChannel::getRole)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_EXIST));
    }

    private Channel findById(String channelId) {
        return jpaChannelRepository.findByIdentification(channelId)
                .orElseThrow(() -> new CustomException(ExceptionEnum.CHANNEL_NOT_FOUND));
    }


    @Override
    public String findByWeffyUser(WeffyUser weffyUser) {
        return jpaSessionRepository.findByWeffyUser(weffyUser)
                .map(Session::getToken)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_EXIST));
    }

}