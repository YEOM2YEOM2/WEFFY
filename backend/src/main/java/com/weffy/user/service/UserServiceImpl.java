package com.weffy.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weffy.mattermost.MattermostHandler;
import com.weffy.mattermost.service.MattermostService;
import com.weffy.token.config.TokenProvider;
import com.weffy.token.entity.RefreshToken;
import com.weffy.token.repository.RefreshTokenRepository;
import com.weffy.token.service.TokenService;
import com.weffy.user.dto.Request.UserSignInReqDto;
import com.weffy.user.dto.Response.UserInfoResDto;
import com.weffy.user.dto.Response.UserMainResDto;
import com.weffy.user.dto.Response.UserSignInResDto;
import com.weffy.user.entity.Role;
import com.weffy.user.entity.WeffyUser;
import com.weffy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MattermostService mattermostService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private MattermostHandler mattermostHandler;

    @Override
    @Transactional
    public UserSignInResDto signIn(UserSignInReqDto signInInfo, String role) {
        ObjectMapper mapper = new ObjectMapper();

        // mattermost 로그인
        String userId = signInInfo.getEmail();
        String password = signInInfo.getPassword();

        ApiResponse<User> userInfo = mattermostHandler.login(userId, password);
        User mmClient = userInfo.readEntity();
        String profileImg = mattermostHandler.image(mmClient.getId());

        WeffyUser weffyUser;
        Optional<WeffyUser> existingUser = userRepository.findByIdentification(mmClient.getId());
        if (userRepository.findByIdentification(mmClient.getId()).isEmpty()) {
            weffyUser = userRepository.save(
                    WeffyUser.builder()
                        .identification(mmClient.getId())
                        .password(passwordEncoder.encode(signInInfo.getPassword()))
                        .email(mmClient.getEmail())
                        .name(mmClient.getLastName() + mmClient.getFirstName())
                        .nickname(mmClient.getNickname())
                        .role((role != null && role.equals("ADMIN"))? Role.ADMIN: Role.USER)
                        .active(true)
                        .profileImg(profileImg)
                        .build()
            );
        } else {
            weffyUser = existingUser.get();
        }

        // Mattermost 세션 토큰
        String token = Objects.requireNonNull(userInfo.getRawResponse().getHeaders().get("Token").get(0).toString());
        mattermostService.saveSession(weffyUser, token);
        // accessToken
        String accessToken = tokenProvider.generateToken(weffyUser,  Duration.ofHours(1));
        //  refreshToken
        String refreshToken = tokenProvider.generateToken(weffyUser,  Duration.ofDays(14));
        Optional<RefreshToken> beforeToken = refreshTokenRepository.findByWeffyUser(weffyUser);
        if(beforeToken.isPresent()) {
            beforeToken.get().updateToken(refreshToken);
        } else {
            refreshTokenRepository.save(
                    RefreshToken
                            .builder()
                            .weffyUser(weffyUser)
                            .refreshToken(refreshToken)
                            .build()
            );
        }
        UserSignInResDto userSignInResDto = new UserSignInResDto().of(mmClient.getId(), accessToken, refreshToken);
        return userSignInResDto;
    }

    @Override
    public UserMainResDto mainUser(String identification) {
        Optional<WeffyUser> user = Optional.ofNullable(userRepository.findByIdentification(identification).orElseThrow(() -> new NoSuchElementException("No user found with id " + identification)));
        UserMainResDto userMainResDto = new UserMainResDto().of(user.get());
        return userMainResDto;
    }
    @Override
    public WeffyUser findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
    }

    @Override
    public UserInfoResDto getUser(WeffyUser weffyUser) {
        return new UserInfoResDto(weffyUser);
    }
}