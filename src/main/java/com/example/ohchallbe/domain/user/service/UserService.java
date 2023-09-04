package com.example.ohchallbe.domain.user.service;

import com.example.ohchallbe.domain.user.dto.*;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.domain.user.entity.UserRoleEnum;
import com.example.ohchallbe.domain.user.handler.EmailSender;
import com.example.ohchallbe.domain.user.repository.UserRepository;
import com.example.ohchallbe.global.dto.TokenDto;
import com.example.ohchallbe.global.entity.RefreshToken;
import com.example.ohchallbe.global.jwt.JwtUtil;
import com.example.ohchallbe.global.repository.RefreshTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailSender emailSender;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(UserSignupRequestDto signupRequestDto) throws MessagingException {
        String email = signupRequestDto.getUseremail();
        String nickname = signupRequestDto.getNickname();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
//        String phonenumber = signupRequestDto.getPhonenumber();
        String userName = signupRequestDto.getUserName();

        Optional<User> checkUsername = userRepository.findByNickname(nickname);

        if(!signupRequestDto.getPassword().equals(signupRequestDto.getPwCheck())){
            System.out.println(signupRequestDto.getPassword() + signupRequestDto.getPwCheck());
            throw new IllegalArgumentException("패스워드가 서로 다릅니다");
        }

        if (!(checkUsername.isEmpty())) {
            throw new IllegalArgumentException("중복된 회원명입니다.");
        }

        Optional<User> checkEmail = userRepository.findByEmail(email);

        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
//        Optional<User> checkPhonenumber = userRepository.findByPhonenumber(phonenumber);
//        if (checkPhonenumber.isPresent()) {
//            throw new IllegalArgumentException("중복된 핸드폰 번호입니다.");
//        }
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 다릅니다.");
            }
            System.out.println("관리자 가입 성공");
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(nickname, password, email, role,userName);
        User saveUser = userRepository.save(user);
        Maildto maildto = Maildto.builder().title(saveUser.getNickname() + "님 환영합니다")
                .message("ochall service를 이용해주셔서 감사합니다")
                .toAddress(saveUser.getEmail()).build();
        emailSender.sendMail(maildto);


    }

    public UserLoginResponseDto login(UserLoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getUseremail()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 메일이거나 비밀번호가 다릅니다."));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        TokenDto tokenDto = jwtUtil.createAllToken(user.getEmail(), user.getRole());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(user.getEmail());
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), user.getEmail());
            refreshTokenRepository.save(newToken);
        }
        return UserLoginResponseDto.builder().refreshToken(tokenDto.getRefreshToken()).accessToken(tokenDto.getAccessToken())
                .errorMsg("").nickname(user.getNickname()).build();
    }

    @Transactional
    public User update(MyPageUpdateRequestDto myPageUpdateRequestDto, String useremail) {
        User user = userRepository.findByEmail(useremail).orElseThrow(() -> new IllegalArgumentException("해당 이메일이 존재하지 않습니다"));

        user.updateUser(myPageUpdateRequestDto);

        return user;


    }
}
