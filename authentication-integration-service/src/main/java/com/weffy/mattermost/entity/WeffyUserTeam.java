package com.weffy.mattermost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weffy.user.entity.WeffyUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeffyUserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "weffyUser_id")
    private WeffyUser weffyUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public WeffyUserTeam(WeffyUser weffyUser, Team team) {
        this.weffyUser = weffyUser;
        this.team = team;
        weffyUser.addTeam(this);
        team.addWeffyUser(this);
    }
}
