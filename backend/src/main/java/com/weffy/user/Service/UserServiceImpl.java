package com.weffy.user.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weffy.mattermost.MattermostHandler;
import com.weffy.user.Dto.Request.UserSignInReqDto;
import com.weffy.user.Dto.Response.UserSignInResDto;
import com.weffy.user.Entity.Role;
import com.weffy.user.Entity.WeffyUser;
import com.weffy.user.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Slf4j
@Transactional
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MattermostHandler mattermostHandler;

    @Override
    @Transactional
    public UserSignInResDto signIn(UserSignInReqDto signInInfo, Role role) {
        ObjectMapper mapper = new ObjectMapper();

        // mattermost 로그인
        String userId = signInInfo.getEmail();
        String password = signInInfo.getPassword();

        ApiResponse<User> userInfo = mattermostHandler.login(userId, password);
        User mmClient = userInfo.readEntity();
        String profile_img = mattermostHandler.image(mmClient.getId());

        WeffyUser weffyUser;
        if (userRepository.findByIdentification(mmClient.getId()).isEmpty()) {
            weffyUser = userRepository.save(
                    WeffyUser.builder()
                        .identification(mmClient.getId())
                        .password(passwordEncoder.encode(signInInfo.getPassword()))
                        .email(mmClient.getEmail())
                        .name(mmClient.getLastName() + mmClient.getFirstName())
                        .nickname(mmClient.getNickname())
                        .role((role==null)? Role.USER: role)
                        .active(true)
                        .profile_img(profile_img)
                        .build()
            );
        }
        String token = Objects.requireNonNull(userInfo.getRawResponse().getHeaders().get("Token").get(0).toString());

        UserSignInResDto userSignInResDto = new UserSignInResDto().of(mmClient.getId(), profile_img, token);
        return userSignInResDto;
    }
}