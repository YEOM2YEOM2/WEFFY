package com.weffy.mattermost.service;



import com.weffy.user.entity.WeffyUser;

import java.io.IOException;

public interface MattermostService {

    void saveSession(WeffyUser weffyUser, String token);

    void saveTeam(String identification, String sessionToken) throws IOException, InterruptedException;
}
