package com.weffy.mattermost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weffy.user.entity.WeffyUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identification;

    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<WeffyUserTeam> weffyUsers = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Channel> channels = new ArrayList<>();

    @Builder
    public Team(String identification, String name) {
        this.identification = identification;
        this.name = name;
    }

    public void addWeffyUser(WeffyUserTeam weffyUserTeam) {
        this.weffyUsers.add(weffyUserTeam);
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }
}
