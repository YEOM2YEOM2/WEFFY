package com.weffy;

import com.weffy.user.Dto.Request.UserSignInReqDto;
import com.weffy.user.Repository.UserRepository;
import com.weffy.user.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeforeTest {
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
}
