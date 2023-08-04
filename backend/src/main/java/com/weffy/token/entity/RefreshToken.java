package com.weffy.token.entity;

import com.weffy.user.entity.WeffyUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weffyuser_id")
    private WeffyUser weffyUser;

    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

    @Builder
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
