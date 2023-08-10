package com.weffy.mattermost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identification;

    private String name;

    private String type;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)
    private List<WeffyUserChannel> weffyUsers = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Channel(String identification, String name, String type, Team team) {
        this.identification = identification;
        this.name = name;
        this.type = type;
        this.team = team;
        team.addChannel(this);
    }

    public void addWeffyUser(WeffyUserChannel weffyUserChannel) {
        this.weffyUsers.add(weffyUserChannel);
    }

}
