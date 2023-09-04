package com.example.ohchallbe.global.security;

import com.example.ohchallbe.domain.user.entity.User;
import com.example.ohchallbe.domain.user.repository.UserRepository;
import com.example.ohchallbe.global.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil ,UserDetailsServiceImpl userDetailsService,UserRepository userRepository){
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getJwtFromHeader(req, "Access");
        String refreshToken = jwtUtil.getJwtFromHeader(req, "Refresh");

        if(accessToken!= null){
            String type = "Access";
            if(jwtUtil.validateToken(accessToken, res, type)){
                setAuthentication(jwtUtil.getEmailFromToken(accessToken));
            }else if(refreshToken != null){
                type = "Refresh";
                boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken, res, type);
                if(isRefreshToken){
                    String email = jwtUtil.getEmailFromToken(refreshToken);
                    User user =  userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("잘못된 이메일입니다."));
                    String newAccessToken = jwtUtil.createToken(email, user.getRole() , "Access");
                    setAuthentication(jwtUtil.getEmailFromToken(newAccessToken.substring(7)));
                    res.setHeader("Access", newAccessToken);
////                    Claims info = jwtUtil.getUserInfoFromToken(newAccessToken.substring(7));
////                    setAuthentication(info.getSubject());
                } else{
                    String a = "{\n \"msg\" : \"Expired AccesToken\"}";
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        res.getWriter().print(a);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    res.setStatus(403);
//                    throw new IllegalArgumentException("잘못된 토큰입니다");
                    return;
                }
            }
        }
        filterChain.doFilter(req, res);
    }
    // 인증 처리
    public void setAuthentication(String username) {
        System.out.println("setAuthentication");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        System.out.println("createAuthentication");
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
