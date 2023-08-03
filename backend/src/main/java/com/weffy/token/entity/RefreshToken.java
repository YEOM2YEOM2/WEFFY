package com.weffy.token.Entity;

import com.weffy.user.Entity.WeffyUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weffyuser_id")
    private WeffyUser weffyUser;

    private String refreshToken;

    public RefreshToken(WeffyUser weffyUser, String refreshToken) {
        this.weffyUser = weffyUser;
        this.refreshToken = refreshToken;
    }

    public RefreshToken(WeffyUser weffyUser) {
        this.weffyUser = weffyUser;
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
