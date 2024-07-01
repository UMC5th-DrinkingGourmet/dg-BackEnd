package com.example.dgbackend.global.jwt.controller;

import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.dto.AuthRequest;
import com.example.dgbackend.global.jwt.dto.AuthResponse;
import com.example.dgbackend.global.jwt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Jwt Token", description = "로그아웃 및 토큰 재발행 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JwtController {

    private final AuthService authService;

    @Operation(summary = "accessToken 재발급")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "accessToken 재발급 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "refreshToken이 만료되었습니다.")
    })
    @PostMapping("/reissue-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {

        return authService.reIssueAccessToken(request);
    }

    @PostMapping("/kakao")
    public ApiResponse<AuthResponse> kakaoLoign(@RequestBody AuthRequest authRequest,
        HttpServletResponse response) throws IOException {
        return ApiResponse.onSuccess(authService.loginOrJoin(response, authRequest));
    }

    @PostMapping("/apple")
    public ApiResponse<AuthResponse> appleLoign(@RequestBody AuthRequest authRequest,
        HttpServletResponse response) throws IOException {
        return ApiResponse.onSuccess(authService.loginOrJoin(response, authRequest));
    }

    @PostMapping("/naver")
    public ApiResponse<AuthResponse> naverLoign(@RequestBody AuthRequest authRequest,
        HttpServletResponse response) throws IOException {
        return ApiResponse.onSuccess(authService.loginOrJoin(response, authRequest));
    }

    @PatchMapping("/logout")
    public ApiResponse<String> logout(HttpServletResponse response,
                                                HttpServletRequest request) throws IOException {
        return ApiResponse.onSuccess(authService.logout(request, response));
    }
}
