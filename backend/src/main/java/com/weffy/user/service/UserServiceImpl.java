package com.weffy.user.service;

import com.weffy.mattermost.MattermostHandler;
import com.weffy.token.dto.response.CreateTokenResDto;
import com.weffy.token.service.RefreshTokenService;
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

import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService tokenService;

    @Autowired
    private MattermostHandler mattermostHandler;

    @Override
    @Transactional
    public UserSignInResDto signUp(UserSignInReqDto signInInfo, String role) {
        // mattermost login
        ApiResponse<User> userInfo = mattermostHandler.login(signInInfo);

        // mattermost user info
        User mmClient = userInfo.readEntity();
        String profileImg = mattermostHandler.image(mmClient.getId());

        WeffyUser weffyUser;
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
            throw new IllegalArgumentException("회원정보가 존재합니다.");
        }

        CreateTokenResDto createTokenResDto = tokenService.createUserToken(userInfo, weffyUser);
        UserSignInResDto userSignInResDto = new UserSignInResDto().of(mmClient.getId(), createTokenResDto);
        return userSignInResDto;
    }
    @Override
    @Transactional
    public UserSignInResDto signIn(UserSignInReqDto signInInfo) {
        ApiResponse<User> userInfo = mattermostHandler.login(signInInfo);
        User mmClient = userInfo.readEntity();

        WeffyUser weffyUser;
        Optional<WeffyUser> existingUser = userRepository.findByIdentification(mmClient.getId());
        if (userRepository.findByIdentification(mmClient.getId()).isEmpty()) {
            throw new IllegalArgumentException("회원정보가 없습니다.");
        } else {
            weffyUser = existingUser.get();
        }

        CreateTokenResDto createTokenResDto = tokenService.createUserToken(userInfo, weffyUser);
        UserSignInResDto userSignInResDto = new UserSignInResDto().of(mmClient.getId(), createTokenResDto);
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