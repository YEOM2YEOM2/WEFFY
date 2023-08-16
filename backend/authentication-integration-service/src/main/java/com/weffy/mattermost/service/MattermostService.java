package com.weffy.mattermost.service;



import com.weffy.mattermost.dto.response.TeamChannelResDto;
import com.weffy.user.entity.WeffyUser;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface MattermostService {

    void saveSession(WeffyUser weffyUser, String token);

    void saveTeam(String identification, String sessionToken) throws IOException, InterruptedException;

    List<TeamChannelResDto> getTeamAndChannel(WeffyUser weffyUser);

    String findByWeffyUser(WeffyUser weffyUser);

    int makeHeaderLink(WeffyUser weffyUser, String channelId) throws IOException, InterruptedException;
}
