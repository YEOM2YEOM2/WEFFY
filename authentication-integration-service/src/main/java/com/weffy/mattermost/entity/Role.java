package com.weffy.mattermost.entity;

import lombok.Getter;

@Getter
public enum Role {
    CHANNEL_USER("CHANNEL_USER"),
    CHANNEL_ADMIN("CHANNEL_ADMIN");
    private String role;
    Role(String role) {
        this.role = role;
    }
}

