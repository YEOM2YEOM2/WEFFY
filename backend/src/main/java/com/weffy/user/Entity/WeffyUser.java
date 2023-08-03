package com.weffy.user.Entity;

import com.weffy.user.util.DateTimeUtil;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WeffyUser {
    @Id
    @GeneratedValue
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

    @CreatedDate
    private String created_at;

    @LastModifiedDate
    private String updated_at;

    @Builder
    public WeffyUser(String identification, String email, String password, String name, String nickname, Role role, Boolean active, String profile_img,  String created_at,  String updated_at) {
        this.identification = identification;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        this.active = active;
        this.profile_img = profile_img;
        this.created_at = DateTimeUtil.getFormattedTime(created_at);
        this.updated_at = DateTimeUtil.getFormattedTime(updated_at);;
    }
}
