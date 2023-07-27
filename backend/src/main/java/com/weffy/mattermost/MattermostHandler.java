package com.weffy.mattermost;

import net.bis5.mattermost.client4.MattermostClient;

import java.util.logging.Level;

public class MattermostHandler {
    public static MattermostClient client() {
        return MattermostClient.builder()
                .url("https://meeting.ssafy.com")
                .logLevel(Level.INFO)
                .ignoreUnknownProperties()
                .build();
    }

}
