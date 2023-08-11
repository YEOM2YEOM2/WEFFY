package com.weffy.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weffy.TestConfig;
import com.weffy.token.config.JwtProperties;
import com.weffy.token.dto.request.CreateAccessTokenReqDto;
import com.weffy.token.entity.RefreshToken;
import com.weffy.token.repository.RefreshTokenRepository;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Map;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class RefreshTokenTest extends TestConfig {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Value("${mysecret.mattermost.email}")
    private String email;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("createNewAccessToken: 새로운 AccessToken 발급")
    public void createNewAccessToken() throws Exception {
        // given
        final String url = "/api/v1/token";
        Optional<WeffyUser> weffyUser = userRepository.findByEmail(email);
        if (weffyUser.isPresent()) {
            String refreshToken = JwtFactory.builder()
                    .claims(Map.of("identification", weffyUser.get().getIdentification()))
                    .build().createToken(jwtProperties);

            refreshTokenRepository.save(new RefreshToken(weffyUser.get(), refreshToken));

            CreateAccessTokenReqDto request = new CreateAccessTokenReqDto();
            request.setRefreshToken(refreshToken);
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            ResultActions resultActions = mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestBody));

            //then
            resultActions
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.accessToken").isNotEmpty());
        }

    }
}
