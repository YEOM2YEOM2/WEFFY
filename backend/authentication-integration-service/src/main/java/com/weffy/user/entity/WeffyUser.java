package com.weffy.user.entity;

import com.weffy.common.entity.TimeEntity;
import com.weffy.mattermost.entity.Team;
import com.weffy.mattermost.entity.WeffyUserChannel;
import com.weffy.mattermost.entity.WeffyUserTeam;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class WeffyUser extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identification;
    private String email;
    private String password;
    private String name;
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private Role role;
    private Boolean active;

    @Column(name = "profile_img")
    private String profileImg;

    @OneToMany(mappedBy = "weffyUser", cascade = CascadeType.REMOVE)
    private List<WeffyUserTeam> teams = new ArrayList<>();

    @OneToMany(mappedBy = "weffyUser", cascade = CascadeType.REMOVE)
    private List<WeffyUserChannel> channels = new ArrayList<>();

    @Builder
    public WeffyUser(String identification, String email, String password, String name, String nickname, Role role, Boolean active, String profileImg) {
        this.identification = identification;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        this.active = active;
        this.profileImg = profileImg;
    }

    public void addTeam(WeffyUserTeam weffyUserTeam) {
        this.teams.add(weffyUserTeam);
    }

    public void addChannel(WeffyUserChannel weffyUserChannel) {
        this.channels.add(weffyUserChannel);
    }
}
