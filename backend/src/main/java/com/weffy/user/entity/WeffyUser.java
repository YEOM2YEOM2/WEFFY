package com.weffy.user.entity;

import com.weffy.common.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
}
