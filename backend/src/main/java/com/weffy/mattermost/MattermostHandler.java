package com.weffy.mattermost;

import com.weffy.user.dto.Request.UserSignInReqDto;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

@Component
public class MattermostHandler {

    @Value("${mysecret.mattermost.session}")
    protected String sessionToken;

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

    public InputStream image(String userId) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://meeting.ssafy.com/api/v4/users/" + userId + "/image")
                .addHeader("Authorization", "Bearer " + sessionToken)
                .build();

        Response response = client.newCall(request).execute();
        InputStream in = response.body().byteStream();
        return in;
    }

}
