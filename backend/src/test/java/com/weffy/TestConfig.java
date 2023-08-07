package com.weffy;

import com.weffy.common.kms.KmsService;
import com.weffy.user.dto.Request.UserSignInReqDto;
import com.weffy.user.repository.UserRepository;
import com.weffy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;

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

    @Autowired
    protected  KmsService kmsService;

    @BeforeEach
    void setUp() {
        user = new UserSignInReqDto();
        user.setEmail(email);
        user.setPassword(kmsService.decryptData(password));
        userService.signIn((HttpServletRequest) user, null);
    }
}
