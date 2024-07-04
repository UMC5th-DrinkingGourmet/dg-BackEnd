package com.example.dgbackend.global.jwt.service;

import com.example.dgbackend.domain.enums.State;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.service.MemberCommandService;
import com.example.dgbackend.domain.member.service.MemberQueryService;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.jwt.dto.AuthRequest;
import com.example.dgbackend.global.jwt.dto.AuthResponse;
import com.example.dgbackend.global.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
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
    private final JwtProvider jwtProvider;
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    /**
     * 회원가입 및 로그인 진행
     */
    public AuthResponse loginOrJoin(HttpServletResponse response, AuthRequest authRequest)
        throws IOException {

        String id = authRequest.getProvider() + "_" + authRequest.getProviderId();

        Long memberId;

        // Provider / Provider 아이디로 가입한 멤버를 찾아옴
        Optional<Member> loginMember = memberQueryService.findByProviderAndProviderId(
            authRequest.getProvider(),
            authRequest.getProviderId());

        boolean isNewMember = true;

        if (loginMember.isEmpty()) {
            if (memberQueryService.existsByNickname(authRequest.getNickName())) {
                throw new ApiException(ErrorStatus._DUPLICATE_NICKNAME);
            }
            Member newMember = MemberRequest.toEntity(authRequest);
            memberCommandService.saveMember(newMember);
            memberId = newMember.getId();

        } else {
            memberId = loginMember.get().getId();
            isNewMember = false;

            if (loginMember.get().getState() == State.REPORTED) {
                throw new ApiException(ErrorStatus._PERMANENTLY_REPORTED_MEMBER);
            } else if (redisUtil.getData(String.valueOf(memberId)).equals("reported")) {
                log.info("일시 정지 -----------", redisUtil.getData(String.valueOf(memberId)));
                throw new ApiException(ErrorStatus._TEMPORARILY_REPORTED_MEMBER);
            }
        }

        // 인증이 성공했을 때, Header에 Access Token, RefreshToken 생성 및 저장
        registerHeaderToken(response, id, "Authorization");
        registerHeaderToken(response, id, "RefreshToken");

        return AuthResponse.toAuthResponse(authRequest.getProvider(), authRequest.getNickName(),isNewMember,
            memberId);

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
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Access Token 추출
        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);

        String refreshToken = request.getHeader("RefreshToken");

        // Access Token 만료
        Date accessTokenExpirationDate= jwtProvider.getAccessTokenExpiration(accessToken);

        // Access Token에서 provider 추출
        String id = jwtProvider.getMemberIdFromToken(refreshToken);

        // Redis에서 해당 Refresh Token 삭제
        redisUtil.deleteData(id);

        // Header에 Access Token 삭제
        response.setHeader("Authorization", "");

        // Redis에 Refresh Token 삭제
        response.setHeader("RefreshToken", "");

        // 현재 시간
        Date currentDate = new Date();

        // 만료 기한과 현재 시간 사이의 밀리초 단위 차이 계산
        long milliseconds = accessTokenExpirationDate.getTime() - currentDate.getTime();

        // Access Token 블랙리스트 추가
        redisUtil.setDataExpire( accessToken,"logout", milliseconds);

        return "로그아웃 하였습니다";
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
        boolean isExists = redisUtil.getData(id).isEmpty();

        // Redis에 refresh token이 만료되어 사라진 경우
        if (!isExists) {
            log.info("-----------------------발급되면 안됨");
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