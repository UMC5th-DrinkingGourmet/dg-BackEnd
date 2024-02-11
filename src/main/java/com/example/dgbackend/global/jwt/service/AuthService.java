package com.example.dgbackend.global.jwt.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.service.MemberCommandService;
import com.example.dgbackend.domain.member.service.MemberQueryService;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.jwt.dto.AuthRequest;
import com.example.dgbackend.global.jwt.dto.AuthResponse;
import com.example.dgbackend.global.util.CookieUtil;
import com.example.dgbackend.global.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    // private final String frontendRedirectUrl = "https://drink-gourmet.kro.kr/myPage";

    /**
     * 회원가입 및 로그인 진행
     */
    public AuthResponse loginOrJoin(HttpServletResponse response, AuthRequest authRequest)
        throws IOException {

        String id = authRequest.getProvider() + "_" + authRequest.getProviderId();

        if (!memberQueryService.existsByProviderAndProviderId(authRequest.getProvider(),
            authRequest.getProviderId())) {
            Member newMember = MemberRequest.toEntity(authRequest);
            memberCommandService.saveMember(newMember);
        }

        // 인증이 성공했을 때, Access Token은 Header에 저장
        registerHeaderToken(response, id);

        // Refresh Token 토큰을 Redis에 저장
        if (redisUtil.getData(id) == null) {
            String refreshToken = jwtProvider.generateRefreshToken(id);
            cookieUtil.create(refreshToken, response);
            log.info("-------------------- cookie에 refresh Token 저장 : {}", refreshToken);
        }

        return AuthResponse.toAuthResponse(authRequest.getProvider(), authRequest.getNickName());
    }

    // Header에 Access Token 담아서 전달
    private void registerHeaderToken(HttpServletResponse response, String id) {

        String token = jwtProvider.generateAccessToken(id);
        response.setHeader("Authorization", token);
        log.info("Token = " + token);
    }

    /**
     * 로그아웃
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // Header에 Access Token 삭제
        response.setHeader("Authorization", "");

        // Redis에 Refresh Token 삭제
        Cookie refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        log.info("Cookie refreshToken : " + refreshToken.getValue());

        String id = jwtProvider.getMemberIdFromToken(refreshToken.getValue());
        redisUtil.deleteData(id);

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
        String id = jwtProvider.getMemberIdFromToken(refreshToken);

        // Redis에 refresh token이 만료되어 사라진 경우
        if (redisUtil.getData(id) == null) {
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