package com.weffy.mattermost.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.weffy.user.entity.WeffyUser;

public interface MattermostService {

    void saveSession(WeffyUser weffyUser, String token);

    void saveTeam(String identification, JsonNode result);
}
