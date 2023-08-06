package com.weffy.mattermost.entity;

import com.weffy.user.entity.WeffyUser;
import com.weffy.common.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Session extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weffyuser_id")
    private WeffyUser weffyUser;

    @Builder
    public Session(String token, WeffyUser weffyUser, LocalDateTime created_at, LocalDateTime updated_at) {
        this.token = token;
        this.weffyUser = weffyUser;
    }
}
