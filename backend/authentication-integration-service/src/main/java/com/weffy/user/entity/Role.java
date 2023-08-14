package com.weffy.user.entity;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    COACH("COACH"),
    CONSULTANT("CONSULTANT"),
    PRO("PRO"),
    TEACHER("TEACHER"),
    USER("USER");
    private String role;
    Role(String role) {
        this.role = role;
    }
}
