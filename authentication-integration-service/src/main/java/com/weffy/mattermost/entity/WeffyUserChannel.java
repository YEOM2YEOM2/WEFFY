package com.weffy.mattermost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weffy.user.entity.WeffyUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeffyUserChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "weffyUser_id")
    private WeffyUser weffyUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "channel_id")
    private Channel channel;


    @Enumerated(value = EnumType.STRING)
    private Role role;


    @Builder
    public WeffyUserChannel(WeffyUser weffyUser, Channel channel, Role role) {
        this.weffyUser = weffyUser;
        this.channel = channel;
        this.role = role;
        weffyUser.addChannel(this);
        channel.addWeffyUser(this);
    }
}
