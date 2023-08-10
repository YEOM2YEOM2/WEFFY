package com.weffy.mattermost.entity;

import lombok.Getter;

@Getter
public enum Role {
    channel_user("CHANNEL_USER"),
    channel_admin("CHANNEL_ADMIN"),;
    private String role;
    Role(String role) {
        this.role = role;
    }
}

