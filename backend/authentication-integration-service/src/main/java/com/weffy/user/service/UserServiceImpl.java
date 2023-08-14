package com.weffy.user.service;

import com.weffy.exception.CustomException;
import com.weffy.exception.ExceptionEnum;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


@Service("userService")
@Transactional
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
    public void signUp(UserSignInReqDto signInInfo, String role) throws IOException{
        // mattermost login
        ApiResponse<User> userInfo = mattermostHandler.login(signInInfo);

        // mattermost user 정보 및 프로필 이미지 저장
        User mmClient = userInfo.readEntity();
        InputStream profileImg = mattermostHandler.image(mmClient.getId());
        BufferedImage bImageFromConvert = ImageIO.read(profileImg);
        String profileUrl = fileService.uploadInputStream(bImageFromConvert, mmClient.getId() + ".png", "weffy");
        String nickName = mmClient.getNickname();
        Optional<WeffyUser> existUser = userRepository.findByIdentification(mmClient.getId());
        if (existUser.isEmpty()) {
            userRepository.save(
                    WeffyUser.builder()
                            .identification(mmClient.getId())
                            .password(passwordEncoder.encode(signInInfo.getPassword()))
                            .email(mmClient.getEmail())
                            .name(mmClient.getLastName() + mmClient.getFirstName())
                            .nickname(nickName)
                            .role((role != null && role.equals("ADMIN"))? Role.ADMIN: getRole(nickName))
                            .active(true)
                            .profileImg(profileUrl)
                            .build()
            );
        } else if (!existUser.get().getActive()){
            throw new CustomException(ExceptionEnum.USER_WITHDRAW);
        }else {
            throw new CustomException(ExceptionEnum.USER_EXIST);
        }
    }

    @Override
    public Role getRole(String nickName) {
        if (nickName.contains("코치")) return Role.COACH;
        else if (nickName.contains("컨설턴트")) return Role.CONSULTANT;
        else if (nickName.contains("프로")) return Role.PRO;
        else if (nickName.contains("강사")) return Role.TEACHER;
        else return Role.USER;
    }

    @Override
    @Transactional
    public UserSignInResDto signIn(UserSignInReqDto signInInfo) throws IOException, InterruptedException {
        ApiResponse<User> userInfo = mattermostHandler.login(signInInfo);
        User mmClient = userInfo.readEntity();

        WeffyUser weffyUser;
        Optional<WeffyUser> existingUser = userRepository.findByIdentification(mmClient.getId());
        if (existingUser.isEmpty()) {
            throw new CustomException(ExceptionEnum.USER_NOT_EXIST);
        } else if (existingUser.get().getActive()){
            weffyUser = existingUser.get();
            // mattermost 로그인이 성공되었지만 mattermost의 비밀번호가 저장된 비밀번호가 다를 때 weffy 내의 비밀번호 수정
            if (!weffyUser.getPassword().equals(signInInfo.getPassword())) {
                setPassword(weffyUser, signInInfo.getPassword());
            }
        } else {
            throw new CustomException(ExceptionEnum.USER_WITHDRAW);
        }

        CreateTokenResDto createTokenResDto = tokenService.createUserToken(userInfo, weffyUser);
        UserSignInResDto userSignInResDto = new UserSignInResDto().of(weffyUser, createTokenResDto);
        return userSignInResDto;
    }

    @Override
    public UserMainResDto mainUser(String identification) {
        Optional<WeffyUser> user = Optional.ofNullable(userRepository.findByIdentification(identification)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_EXIST)));
        UserMainResDto userMainResDto = new UserMainResDto().of(user.get());
        return userMainResDto;
    }
    @Override
    public WeffyUser findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_EXIST));
    }

    @Override
    public WeffyUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_EXIST));
    }

    @Override
    public UserInfoResDto getUser(WeffyUser weffyUser) {
        return new UserInfoResDto(weffyUser);
    }

    @Override
    @Transactional
    public void setUser(WeffyUser weffyUser, MultipartFile profileImg, String nickName) {
        if(!profileImg.isEmpty()) {
            String img = fileService.uploadFile(profileImg, null, "weffy").getUrl();
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

    @Override
    @Transactional
    public void deleteUser(WeffyUser weffyUser) {
        weffyUser.setActive(false);
        userRepository.save(weffyUser);
    }
}