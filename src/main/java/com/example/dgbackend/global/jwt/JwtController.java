package com.example.dgbackend.global.jwt;

import com.example.dgbackend.global.security.oauth2.service.AuthService;
import com.example.dgbackend.global.util.CookieUtil;
import com.example.dgbackend.global.util.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Jwt Token", description = "로그아웃 및 토큰 재발행 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JwtController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;

    @Operation(summary = "로그아웃 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공 & Cookie에 Refresh Token 삭제")
    })
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
    }

    @Operation(summary = "accessToken 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "accessToken 재발급 성공"),
            @ApiResponse(responseCode = "403", description = "refreshToken이 만료되었습니다.")
    })
    @GetMapping("/token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

        Cookie refreshTokenCookie = cookieUtil.getCookie(request, "refreshToken");
        // Cookie에 refresh Token이 없는 경우
        if (refreshTokenCookie == null) {
            // throw new
        }

        String refreshToken = refreshTokenCookie.getValue();
        String memberEmail = jwtProvider.getMemberEmailFromToken(refreshToken);

        // Redis에 refresh token이 만료되어 사라진 경우
        if (redisUtil.getData(memberEmail) == null) {
            // throw new
        }

        /**
         * Access Token 재발급
         */
        String newAcessToken;
        newAcessToken = jwtProvider.refreshAccessToken(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", newAcessToken);

        jwtProvider.getAuthenticationFromToken(newAcessToken);

        return ResponseEntity.ok().headers(httpHeaders).build();
    }
}
