package com.example.dgbackend.global.util;

import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    // Cookie에 Refresh Token 등록
    public void create(String value, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", value)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(false)
                .maxAge(Integer.MAX_VALUE) // 리프레시 토큰은 브라우저를 닫더라도 계속 유지
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    // Cookie 삭제
    public void delete(String value, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", value)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(false)
                .maxAge(0) // 리프레쉬 토큰 제거
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    // Cookie에서 Refresh Token 얻기
    public Cookie getRefreshTokenFromCookie(HttpServletRequest request) {
        return getCookie(request, "refreshToken").orElseThrow(
                () -> new ApiException(ErrorStatus._REFRESH_TOKEN_NOT_FOUND)
        );
    }

    // 특정 이름의 Cookie 얻기
    public Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();

        return Optional.ofNullable(cookies)
                .flatMap(cookieArray -> Arrays.stream(cookieArray)
                        .filter(cookie -> cookie.getName().equals(cookieName))
                        .findFirst());
    }
}
