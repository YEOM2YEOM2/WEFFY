package com.weffy;

import com.weffy.user.Dto.Request.UserSignInReqDto;
import com.weffy.user.Repository.UserRepository;
import com.weffy.user.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestConfig {
    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Value("${mysecret.mattermost.email}")
    protected String email;
    @Value("${mysecret.mattermost.password}")
    protected String password;

    protected UserSignInReqDto user;

    @BeforeEach
    void setUp() {
        user = new UserSignInReqDto();
        user.setEmail(email);
        user.setPassword(password);
        userService.signIn(user, null);
    }
}
