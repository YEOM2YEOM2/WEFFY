package com.weffy.user.dto.Response;

import com.weffy.user.entity.Role;
import com.weffy.user.entity.WeffyUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResDto {
    String identification;
    String email;
    String name;
    String nickname;
    String profileImg;
    Role role;
    Boolean active;

    public UserInfoResDto(WeffyUser weffyUser) {
        this.identification = weffyUser.getIdentification();
        this.email = weffyUser.getEmail();
        this.name = weffyUser.getName();
        this.nickname = weffyUser.getNickname();
        this.profileImg = weffyUser.getProfileImg();
        this.role = weffyUser.getRole();
        this.active = weffyUser.getActive();
    }
}
