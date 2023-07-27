package com.weffy.user.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
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
    private byte[] profile_img;

    @CreatedDate
    private String created_at;

    @LastModifiedDate
    private String updated_at;

    public String getFormattedCreatedAt() {
        return formatDateTime(created_at);
    }

    public String getFormattedUpdatedAt() {
        return formatDateTime(updated_at);
    }

    private String formatDateTime(String localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(String.valueOf(formatter));
    }

    @Builder
    public User(String identification, String email, String password, String name, String nickname, Role role, Boolean active, byte[] profile_img) {
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
