package com.weffy.mattermost;

import com.weffy.user.dto.Request.UserSignInReqDto;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.User;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@Component
public class MattermostHandler {

    public MattermostClient client() {
        return MattermostClient.builder()
                .url("https://meeting.ssafy.com")
//                .logLevel(Level.INFO)
                .ignoreUnknownProperties()
                .build();
    }

    public ApiResponse<User> login(UserSignInReqDto userSignInReqDto) {
        // mattermost 로그인
        String email = userSignInReqDto.getEmail();
        String password = userSignInReqDto.getPassword();

        return client().login(email, password);
    }

    public String image(String userId) {
        return "https://meeting.ssafy.com" + client().getUserProfileImageRoute(userId);
    }



}
