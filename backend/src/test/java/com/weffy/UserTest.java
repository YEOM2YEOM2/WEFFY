package com.weffy;

import com.weffy.user.Dto.Request.UserSignInReqDto;
import com.weffy.user.Entity.WeffyUser;
import com.weffy.user.Repository.UserRepository;
import com.weffy.user.Service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Value("${mysecret.mattermost.email}")
    private String email;
    @Value("${mysecret.mattermost.password}")
    private String password;

    private UserSignInReqDto user;

    @BeforeEach
    void setUp() {
        user = new UserSignInReqDto();
        user.setEmail(email);
        user.setPassword(password);
        userService.signIn(user, null);
    }

    @Test
    @Transactional
    @DisplayName("회원가입이 성공하여야한다.")
    void createUser_O() {
        Optional<WeffyUser> user = userRepository.findByIdentification("1ejz9ks8kpy7uyb4rsn4s44pse");
        //then
        Assertions.assertThat(user.get().getEmail()).isEqualTo(email);
    }
}
