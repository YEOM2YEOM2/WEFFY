package com.weffy.user.service;

import com.weffy.file.service.FileService;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService tokenService;
    private final FileService fileService;

    @Autowired
    private MattermostHandler mattermostHandler;

    @Override
    @Transactional
    public UserSignInResDto signUp(HttpServletRequest request,UserSignInReqDto signInInfo, String role) throws IOException{
        // mattermost login
        ApiResponse<User> userInfo = mattermostHandler.login(signInInfo);

        // mattermost user info
        User mmClient = userInfo.readEntity();
        InputStream profileImg = mattermostHandler.image(mmClient.getId());

        BufferedImage bImageFromConvert = ImageIO.read(profileImg);

        String profileUrl = fileService.uploadInputStream(bImageFromConvert, mmClient.getId() + ".png");
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
                            .profileImg(profileUrl)
                            .build()
            );
        } else {
            throw new IllegalArgumentException("회원정보가 존재합니다.");
        }

        CreateTokenResDto createTokenResDto = tokenService.createUserToken(request, userInfo, weffyUser);
        UserSignInResDto userSignInResDto = new UserSignInResDto().of(mmClient.getId(), createTokenResDto);
        return userSignInResDto;
    }
    @Override
    @Transactional
    public UserSignInResDto signIn(HttpServletRequest request, UserSignInReqDto signInInfo) {
        ApiResponse<User> userInfo = mattermostHandler.login(signInInfo);
        User mmClient = userInfo.readEntity();

        WeffyUser weffyUser;
        Optional<WeffyUser> existingUser = userRepository.findByIdentification(mmClient.getId());
        if (userRepository.findByIdentification(mmClient.getId()).isEmpty()) {
            throw new IllegalArgumentException("회원정보가 없습니다.");
        } else {
            weffyUser = existingUser.get();
            // mattermost 로그인이 성공되었지만 mattermost의 비밀번호가 저장된 비밀번호가 다를 때 weffy 내의 비밀번호 수정
            if (!weffyUser.getPassword().equals(signInInfo.getPassword())) {
                setPassword(weffyUser, signInInfo.getPassword());
            }
        }

        CreateTokenResDto createTokenResDto = tokenService.createUserToken(request, userInfo, weffyUser);
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

    @Override
    @Transactional
    public void setUser(WeffyUser weffyUser, MultipartFile profileImg, String nickName) {
        if(!profileImg.isEmpty()) {
            String img = fileService.uploadFile(profileImg);
            weffyUser.setProfileImg(img);
        }
        if(!nickName.isEmpty()) weffyUser.setNickname(nickName);
        userRepository.save(weffyUser);
    }

    @Override
    @Transactional
    public void setPassword(WeffyUser weffyUser, String password) {
        weffyUser.setPassword(passwordEncoder.encode(password));
    }
}