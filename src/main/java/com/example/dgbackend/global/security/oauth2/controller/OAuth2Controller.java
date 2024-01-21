package com.example.dgbackend.global.security.oauth2.controller;

import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.security.oauth2.service.CustomOAuth2UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "OAuth2.0", description = "소셜 로그인 API")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Operation(summary = "카카오 로그인 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    })
    @GetMapping("/kakao")
    public void kakaoLogin() {
    }

    @Operation(summary = "네이버 로그인 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    })
    @GetMapping("/naver")
    public void naverLogin() {
    }

    @Operation(summary = "애플 로그인 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    })
    @GetMapping("/apple")
    public void appleLogin() {
    }

}
