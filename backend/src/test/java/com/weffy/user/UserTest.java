package com.weffy.user;

import com.weffy.TestConfig;
import com.weffy.user.Dto.Response.UserSignInResDto;
import com.weffy.user.Entity.WeffyUser;
import com.weffy.user.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserTest extends TestConfig {

    @Autowired
    private UserRepository userRepository;

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
    void signInUser_O() {
        UserSignInResDto res = userService.signIn(user, null);
        Optional<WeffyUser> testUser = userRepository.findByEmail(email);
        //then
        Assertions.assertThat(testUser.get().getIdentification()).isEqualTo(res.getUserId());
    }
}
