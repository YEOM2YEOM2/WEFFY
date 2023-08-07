package com.weffy.mattermost;

import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
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

    // Mattermost 연결
    public MattermostClient client() {
        return MattermostClient.builder()
                .url("https://meeting.ssafy.com")
//                .logLevel(Level.INFO)
                .ignoreUnknownProperties()
                .build();
    }

    // Mattermost 로그인
    public ApiResponse<User> login(UserSignInReqDto userSignInReqDto) {
        String email = userSignInReqDto.getEmail();
        String password = userSignInReqDto.getPassword();

        ApiResponse<User> response = client().login(email, password);

        if (response.getRawResponse().getStatus() == 200) {
            return response;
        } else {
            throw new CustomException(ExceptionEnum.MATTERMOSTLOGINFAILED);
        }
    }

    // Mattermost profile image 불러오기
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
