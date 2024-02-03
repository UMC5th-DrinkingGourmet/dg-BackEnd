package com.example.dgbackend.global.security.oauth2.service;

import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.util.CookieUtil;
import com.example.dgbackend.global.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    /**
     * Access Token 재발급
     */
    public ResponseEntity<?> reIssueAccessToken(HttpServletRequest request) {

        Cookie refreshTokenCookie = cookieUtil.getCookie(request, "refreshToken").orElseThrow(
                () -> new ApiException(ErrorStatus._REFRESH_TOKEN_NOT_FOUND)
        );

        String refreshToken = refreshTokenCookie.getValue();
        String memberEmail = jwtProvider.getMemberEmailFromToken(refreshToken);

        // Redis에 refresh token이 만료되어 사라진 경우
        if (redisUtil.getData(memberEmail) == null) {
            throw new ApiException(ErrorStatus._REDIS_NOT_FOUND);
        }

        // Access Token 재발급
        String newAcessToken;
        newAcessToken = jwtProvider.refreshAccessToken(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", newAcessToken);

        jwtProvider.getAuthenticationFromToken(newAcessToken);

        return ResponseEntity.ok().headers(httpHeaders).build();

    }
}
