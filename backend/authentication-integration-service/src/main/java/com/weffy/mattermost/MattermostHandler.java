package com.weffy.mattermost;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.mattermost.entity.Role;
import com.weffy.mattermost.service.MattermostService;
import com.weffy.user.dto.Request.UserSignInReqDto;
import lombok.RequiredArgsConstructor;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
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

    HttpClient teamClient = HttpClient.newHttpClient();
    // MattermostHandler
    public JsonNode getTeam(String identification, String sessionToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://meeting.ssafy.com/api/v4/users/" + identification + "/teams"))
                .header("Authorization", "Bearer " + sessionToken)
                .GET()
                .build();

        HttpResponse<String> teamInfo = teamClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // HttpResponse의 body를 JSON으로 파싱
        return objectMapper.readTree(teamInfo.body());
    }


    HttpClient channelClient = HttpClient.newHttpClient();
    public JsonNode getChannel(String identification, String teamId, String sessionToken) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://meeting.ssafy.com/api/v4/users/" + identification + "/teams/" + teamId + "/channels"))
                .header("Authorization", "Bearer " + sessionToken)
                .GET()
                .build();

        HttpResponse<String> channelInfo = channelClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // HttpResponse의 body를 JSON으로 파싱
       return objectMapper.readTree(channelInfo.body());
    }

    HttpClient roleClient = HttpClient.newHttpClient();
    public Role getChannelRole(String identification, String channelId, String sessionToken) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://meeting.ssafy.com/api/v4/channels/" + channelId + "/members/" + identification))
                .header("Authorization", "Bearer " + sessionToken)
                .GET()
                .build();

        HttpResponse<String> channelInfo = channelClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // HttpResponse의 body를 JSON으로 파싱
        String role = objectMapper.readTree(channelInfo.body()).get("roles").asText();
        role = role.equals("channel_user channel_admin") ? "channel_admin" : role;
        return Role.valueOf(role);
    }
}
