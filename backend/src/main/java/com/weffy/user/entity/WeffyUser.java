package com.weffy.user.entity;

import com.weffy.common.entity.TimeEntity;
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
    private String profile_img;

    @Builder
    public WeffyUser(String identification, String email, String password, String name, String nickname, Role role, Boolean active, String profile_img) {
        this.identification = identification;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        this.active = active;
        this.profile_img = profile_img;
    }
}
