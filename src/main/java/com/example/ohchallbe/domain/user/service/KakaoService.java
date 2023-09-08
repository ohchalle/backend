package com.example.ohchallbe.domain.user.service;

import com.example.ohchallbe.domain.user.dto.KakaoUserInfoDto;
import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.domain.user.entity.UserRoleEnum;
import com.example.ohchallbe.domain.user.repository.UserRepository;
import com.example.ohchallbe.global.dto.TokenDto;
import com.example.ohchallbe.global.jwt.JwtUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Access;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public TokenDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        System.out.println("token dto 나오는지3");
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessAuthorizationToken = getToken(code);
        System.out.println("token dto 나오는지4");
        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessAuthorizationToken);
        System.out.println("token dto 나오는지5");
        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
        System.out.println("token dto 나오는지6");

        // 4. JWT 토큰 반환
        String accessToken = jwtUtil.createToken(kakaoUser.getEmail(), kakaoUser.getRole(), "Access");
        System.out.println("token dto 나오는지7");
        String refreshToken = jwtUtil.createToken(kakaoUser.getEmail(), kakaoUser.getRole(), "Refresh");
        System.out.println("token dto 나오는지8");


        response.setHeader(JwtUtil.ACCESS_HEADER, accessToken);
        response.setHeader(JwtUtil.REFRESH_HEADER, refreshToken);

        return new TokenDto(accessToken, refreshToken);
    }

    private String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "3c25ce3be60d236b1624d94bf5c86eb5");
        body.add("redirect_uri", "https://ohchalle.com/oauth");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessAuthorizationToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessAuthorizationToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);
        try{
            if (kakaoUser == null) {
                // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
                String kakaoEmail = kakaoUserInfo.getEmail();
                User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
                if (sameEmailUser != null) {
                    kakaoUser = sameEmailUser;
                    // 기존 회원정보에 카카오 Id 추가
                    kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
                } else {
                    // 신규 회원가입
                    // password: random UUID
                    String password = UUID.randomUUID().toString();
                    String encodedPassword = passwordEncoder.encode(password);

                    // email: kakao email
                    String email = kakaoUserInfo.getEmail();

                    kakaoUser = new User(kakaoUserInfo.getNickname(), encodedPassword, email, UserRoleEnum.USER, kakaoId);

                }

                userRepository.save(kakaoUser);
            }
        }catch(Exception e){
            throw new RuntimeException(e+"카카오 저장오류");
        }

        return kakaoUser;
    }

}