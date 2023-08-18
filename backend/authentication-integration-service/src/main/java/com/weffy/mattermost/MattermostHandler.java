package com.weffy.mattermost;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
import com.weffy.mattermost.entity.Role;
import com.weffy.user.dto.Request.UserSignInReqDto;
import lombok.RequiredArgsConstructor;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.User;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
            throw new CustomException(ExceptionEnum.MATTERMOST_LOGIN_FAILED);
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

    // Mattermost 팀 정보 가져오기
    HttpClient teamClient = HttpClient.newHttpClient();
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



    // Mattermost 채널 정보 가져오기
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

    // Mattermost 채널 권한 가져오기
    HttpClient roleClient = HttpClient.newHttpClient();
    public Role getChannelRole(String identification, String channelId, String sessionToken) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://meeting.ssafy.com/api/v4/channels/" + channelId + "/members/" + identification))
                .header("Authorization", "Bearer " + sessionToken)
                .GET()
                .build();

        HttpResponse<String> rolelInfo = roleClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // HttpResponse의 body를 JSON으로 파싱
        String role = objectMapper.readTree(rolelInfo.body()).get("roles").asText();
        role = role.equals("channel_user channel_admin") ? "channel_admin" : role;
        return Role.valueOf(role);
    }

    // Mattermost 채널 헤더에 conference 링크 달기
    HttpClient beforeClient = HttpClient.newHttpClient();
    HttpClient putClient = HttpClient.newHttpClient();
    public int putHeaderLink(String channelId, String sessionToken) throws IOException, InterruptedException {
        // 이전 channel의 정보를 가져오기
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://meeting.ssafy.com/api/v4/channels/" + channelId))
                .header("Authorization", "Bearer " + sessionToken)
                .GET()
                .build();

        HttpResponse<String> channelInfo = beforeClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // HttpResponse의 body를 JSON으로 파싱
        String name = objectMapper.readTree(channelInfo.body()).get("name").asText();
        String display_name = objectMapper.readTree(channelInfo.body()).get("display_name").asText();
        String purpose = objectMapper.readTree(channelInfo.body()).get("purpose").asText();
        String header = objectMapper.readTree(channelInfo.body()).get("header").asText();

        // 헤더에 weffy 링크 넣기

        // 헤더에 weffy 링크 넣어서 JSON으로 다시 묶기
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonObject = mapper.createObjectNode();

        jsonObject.put("id", channelId);
        jsonObject.put("name", name);
        jsonObject.put("display_name", display_name);
        jsonObject.put("purpose", purpose);
        jsonObject.put("header", header + "|[ :weffy_logo:  Start WEFFY](http://ssafy-weffy.s3-website.ap-northeast-2.amazonaws.com/meeting/" + channelId + ")");

        String requestBodyData = jsonObject.toString();

        HttpRequest putHeader = HttpRequest.newBuilder()
                .uri(URI.create("https://meeting.ssafy.com/api/v4/channels/" + channelId))
                .header("Authorization", "Bearer " + sessionToken)
                .PUT(HttpRequest.BodyPublishers.ofString(requestBodyData))
                .build();

        return putClient.send(putHeader, HttpResponse.BodyHandlers.ofString()).statusCode();
    }
}