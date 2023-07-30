package com.weffy.config.jwt;

import com.weffy.TestConfig;
import com.weffy.token.JwtProperties;
import com.weffy.token.TokenProvider;
import com.weffy.user.Entity.WeffyUser;
import com.weffy.user.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
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

    @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
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
        }
    }
}
