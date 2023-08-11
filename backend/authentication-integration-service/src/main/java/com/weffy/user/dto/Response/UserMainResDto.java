package com.weffy.user.dto.Response;

import com.weffy.user.entity.WeffyUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMainResDto {

    String identification;
    String nickName;
    String profileImg;

    public UserMainResDto of(WeffyUser weffyUser) {
        UserMainResDto res = new UserMainResDto();
        res.identification = weffyUser.getIdentification();
        res.nickName = weffyUser.getNickname();
        res.profileImg = weffyUser.getProfileImg();
        return res;
    }
}
