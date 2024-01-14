package com.example.dgbackend.global.security.oauth2.controller;

import com.example.dgbackend.global.security.oauth2.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth2.0", description = "소셜 로그인 API")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    })
    @GetMapping("/login/kakao")
    public void kakaoLogin() {
    }

    @Operation(summary = "네이버 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    })
    @GetMapping("/login/naver")
    public void naverLogin() {
    }

    @Operation(summary = "애플 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    })
    @GetMapping("/login/apple")
    public void appleLogin() {
    }

    @Operation(summary = "로그아웃 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공 & Cookie에 Refresh Token 삭제")
    })
    @GetMapping("/auth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
    }
}
