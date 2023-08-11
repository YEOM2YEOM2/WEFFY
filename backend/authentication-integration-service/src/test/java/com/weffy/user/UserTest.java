package com.weffy.user;

import com.weffy.TestConfig;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import com.weffy.user.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
public class UserTest extends TestConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Value("${mysecret.mattermost.email}")
    protected String email;
    @Value("${mysecret.mattermost.password}")
    protected String password;

    @Test
    @Transactional
    @DisplayName("회원가입이 성공하여야한다.")
    void createUser_O() {
        Optional<WeffyUser> testUser = userRepository.findByIdentification("1ejz9ks8kpy7uyb4rsn4s44pse");
        //then
        Assertions.assertThat(testUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    @Transactional
    @DisplayName("로그인이 성공하여야한다.")
    void signInUser_O() throws IOException, InterruptedException {
        UserSignInResDto res = userService.signIn(user);
        Optional<WeffyUser> testUser = userRepository.findByEmail(email);
        //then
        Assertions.assertThat(testUser.get().getIdentification()).isEqualTo(res.getIdentification());
    }
}
