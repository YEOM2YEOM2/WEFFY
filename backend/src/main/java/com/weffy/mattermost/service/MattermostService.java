package com.weffy.mattermost.service;


import com.weffy.user.entity.WeffyUser;

public interface MattermostService {

    void saveSession(WeffyUser weffyUser, String token);
}
