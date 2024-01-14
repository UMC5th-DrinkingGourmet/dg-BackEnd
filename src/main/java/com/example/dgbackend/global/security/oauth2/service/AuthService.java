package com.example.dgbackend.global.security.oauth2.service;

import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.util.CookieUtil;
import com.example.dgbackend.global.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;

    /**
     * 로그아웃
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // Header에 Access Token 삭제
        response.setHeader("Authorization", "");

        // Redis에 Refresh Token 삭제
        Cookie refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        log.info("Cookie refreshToken : " + refreshToken.getValue());

        String memberEmail = jwtProvider.getMemberEmailFromToken(refreshToken.getValue());
        redisUtil.deleteData(memberEmail);

        // Cookie에 저장된 Refresh 삭제
        cookieUtil.delete("", response);
    }
}
