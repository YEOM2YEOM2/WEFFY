package com.weffy.token;

import com.weffy.TestConfig;
import com.weffy.user.Entity.WeffyUser;
import com.weffy.user.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class TokenTest extends TestConfig{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private JwtProperties jwtProperties;
    @Value("${mysecret.mattermost.email}")
    private String email;

    @Test
    @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    void generateToken_O() {
        //given
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(email);
        if (weffyUser.isPresent()) {
            //when
            String token = tokenProvider.generateToken(weffyUser.get(), Duration.ofDays(14));
            //then
            String identification = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("identification", String.class);

            Assertions.assertThat(identification).isEqualTo(weffyUser.get().getIdentification());
        } else {
            Assertions.fail(email + "을 가진 user가 없습니다.");
        }
    }

    @Test
    @DisplayName("validToken() : 만료된 토큰일 때 유효성 검증에 실패한다.")
    void validToken_X() {
        //given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);
        //when
        boolean result = tokenProvider.validToken(token);
        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("validToken() : 유효한 토큰일 때 유효성 검증에 성공한다.")
    void validToken_O() {
        //given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
        //when
        boolean result = tokenProvider.validToken(token);
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("getAuthentication() : 토큰 기반으로 인증 정보를 가져올 수 있다.")
    void getAuthentication_O() {
        //given
        String token = JwtFactory.builder().subject(email).build().createToken(jwtProperties);
        //when
        Authentication authentication = tokenProvider.getAuthentication(token);
        //then
        Assertions.assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(email);
    }

    @Test
    @DisplayName("getUserId() : 토큰 기반으로 user의 identification을 가져올 수 있다.")
    void getUserId_O() {
        //given
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(email);
        if (weffyUser.isPresent()) {
            String userID = weffyUser.get().getIdentification();
            String token = JwtFactory.builder().claims(Map.of("identification", userID)).build().createToken(jwtProperties);
            //when
            String userIdentification = tokenProvider.getUserId(token);
            //then
            Assertions.assertThat(userIdentification).isEqualTo(userID);
        } else {
            Assertions.fail(email + "을 가진 user가 없습니다.");
        }
    }
}
