package com.example.ohchallbe.domain.user.controller;


import com.example.ohchallbe.domain.user.dto.*;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.domain.user.service.KakaoService;
import com.example.ohchallbe.domain.user.service.UserService;
import com.example.ohchallbe.global.dto.TokenDto;
import com.example.ohchallbe.global.jwt.JwtUtil;
import com.example.ohchallbe.global.message.MessageResponseDto;
import com.example.ohchallbe.global.security.UserDetailsImpl;
import com.example.ohchallbe.global.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "2조 api" , description = "추후 계속 업데이트 됩니다")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final KakaoService kakaoService;

    @Operation(summary = "카카오 회원가입입니다", description = "카카오 회원가입 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @GetMapping("/user/kakao/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 토큰 반환

        System.out.println("token dto 나오는지");
        TokenDto tokenDto = kakaoService.kakaoLogin(code, response);
        System.out.println("token dto 나오는지2");


//        HttpHeaders headers = new HttpHeaders();
//
//        headers.add(JwtUtil.ACCESS_HEADER, tokenDto.getAccessToken()); // 액세스 토큰을 Authorization 헤더에 추가
//
//        headers.add(JwtUtil.REFRESH_HEADER, tokenDto.getRefreshToken()); // 리프레시 토큰을 Refresh 헤더에 추가

        return ResponseEntity.ok("성공");
    }


    @Operation(summary = "회원가입입니다", description = "회원가입 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })

    @PostMapping( "/signup")
    public MessageResponseDto signup(@RequestBody UserSignupRequestDto requestDto){
        try {
            userService.signup(requestDto);
        }catch (Exception e){
            return new MessageResponseDto(e.getMessage());
        }


        return new MessageResponseDto("회원 가입 성공");
    }

    @Operation(summary = "로그인 입니다", description = "로그인 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })
    @PostMapping( "/login")
    private ResponseEntity<?> login(@RequestBody UserLoginRequestDto userloginRequestDto, HttpServletResponse response) {
        try {
            UserLoginResponseDto userLoginResponseDto = userService.login(userloginRequestDto);
            response.addHeader(JwtUtil.ACCESS_HEADER, userLoginResponseDto.getAccessToken());
            response.addHeader(JwtUtil.REFRESH_HEADER, userLoginResponseDto.getRefreshToken());
            return ResponseEntity.ok(LoginResultDto.builder().nickname(userLoginResponseDto.getNickname()).isSucces(true).build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDto(e.getMessage()));
        }


    }

    @Operation(summary = "mypage 정보 가져옵니다", description = "mypage api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MyPageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MyPageResponseDto.class)))
    })
    @GetMapping("/auth/mypage")
    public ResponseEntity<MyPageResponseDto> getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails){


        User user = userDetails.getUser();


        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder().userName(user.getUserName()).userEmail(user.getEmail())
                .nickname(user.getNickname()).build();

        return ResponseEntity.ok(myPageResponseDto);
    }

    @Operation(summary = "회원정보수정 입니다", description = "회원정보수정 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    @PutMapping("/auth/mypage")
    public ResponseEntity<?> updateMypage(@RequestBody MyPageUpdateRequestDto myPageUpdateRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){


        User user = userDetails.getUser();
        User newUser = userService.update(myPageUpdateRequestDto, user.getEmail());


       return ResponseEntity.ok(newUser.getModifiedAt());

    }


}
