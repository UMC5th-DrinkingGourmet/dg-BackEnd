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

        // 인증이 성공했을 때, Header에 Access Token, RefreshToken 생성 및 저장
        registerHeaderToken(response, id, "Authorization");
        registerHeaderToken(response, id, "RefreshToken");

        return AuthResponse.toAuthResponse(authRequest.getProvider(), authRequest.getNickName());
    }

    // Header에 Access Token 담아서 전달
    private void registerHeaderToken(HttpServletResponse response, String id, String HeaderName) {

        if (HeaderName.equals("Authorization")) {
            String accessToken = jwtProvider.generateAccessToken(id);
            response.setHeader(HeaderName, accessToken);
            log.info("Access Token = " + accessToken);
        } else {
            String refreshToken = jwtProvider.generateRefreshToken(id);
            response.setHeader(HeaderName, refreshToken);
            log.info("Refresh Token = " + refreshToken);
        }

    }

    /**
     * 로그아웃
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // Header에 Access Token 삭제
        response.setHeader("Authorization", "");

        // Redis에 Refresh Token 삭제
        String refreshToken = request.getHeader("RefreshToken");
        String id = jwtProvider.getMemberIdFromToken(refreshToken);
        redisUtil.deleteData(id);
        response.setHeader("RefreshToken", "");
    }

    /**
     * Access Token 재발급
     */
    public ResponseEntity<?> reIssueAccessToken(HttpServletRequest request) {

        String refreshToken = request.getHeader("RefreshToken");

        if (refreshToken == null) {
            throw new ApiException(ErrorStatus._EMPTY_JWT);
        }

        String id = jwtProvider.getMemberIdFromToken(refreshToken);
        // Redis에 refresh token이 만료되어 사라진 경우
        if (redisUtil.getData(id) == null) {
            throw new ApiException(ErrorStatus._REDIS_NOT_FOUND);
        }

        // Access Token 재발급
        String newAcessToken;
        newAcessToken = jwtProvider.reissueAccessToken(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", newAcessToken);

        jwtProvider.getAuthenticationFromToken(newAcessToken);
        log.info(
            "============================================= Access Token 재발급 : " + newAcessToken);

        return ResponseEntity.ok().headers(httpHeaders).build();
    }
}