package com.example.dgbackend.global.security.oauth2.handler;

import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.jwt.JwtProvider;
import com.example.dgbackend.global.security.oauth2.dto.response.AuthResponse;
import com.example.dgbackend.global.security.oauth2.principal.CustomOAuth2User;
import com.example.dgbackend.global.security.oauth2.userInfo.KakaoUserInfo;
import com.example.dgbackend.global.util.CookieUtil;
import com.example.dgbackend.global.util.RedisUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // TODO: 추후 프론트 주소로 변경
    private final String frontendRedirectUrl = "http://localhost:8080/myPage";
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        SocialType socialType = customOAuth2User.getMember().getSocialType();
        String email = null;

        if (socialType.equals(SocialType.KAKAO)) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(customOAuth2User.getAttributes());
            email = kakaoUserInfo.getEmail();
        } else if (socialType.equals(SocialType.NAVER)) {
            return;
        } else if (socialType.equals(SocialType.APPLE)) {
            return;
        }

        log.info(frontendRedirectUrl + "----------------------------- frontendRedirectUrl");

        Member loginMember = memberRepository.findByEmailAndSocialType(email, socialType).get();
        String loginMemberEmail = loginMember.getEmail();

        // 인증이 성공했을 때, Access Token은 Header에 저장
        registerHeaderToken(response, loginMemberEmail);

        // Refresh Token 토큰을 Redis에 저장
        if (redisUtil.getData(loginMemberEmail) == null) {
            String refreshToken = jwtProvider.generateRefreshToken(loginMemberEmail);

            // refresh token 쿠키에 담아서 전달
            cookieUtil.create(refreshToken, response);
            log.info("-------------------- cookie에 refresh Token 저장 : {}", refreshToken);
        }

        AuthResponse authResponse = AuthResponse.builder()
                .memberId(loginMember.getId())
                .email(loginMemberEmail)
                .nickName(loginMember.getNickName())
                .profileImageUrl(loginMember.getProfileImageUrl())
                .socialType(String.valueOf(loginMember.getSocialType()))
                .build();

        registerResponse(response, authResponse);
    }

    // Header에 Access Token 담아서 전달
    private void registerHeaderToken(HttpServletResponse response, String loginMemberEmail) {
        String token = jwtProvider.generateAccessToken(loginMemberEmail);

        response.setHeader("Authorization", token);
        log.info("Token = " + token);
    }

    // 로그인에 성공한 사용자에게 발급된 인증 정보를 사용하여 프론트엔드로 리다이렉트
    private void registerResponse(HttpServletResponse response, AuthResponse authResponse) throws IOException {

        String encodedMemberId = URLEncoder.encode(String.valueOf(authResponse.getMemberId()), StandardCharsets.UTF_8);
        String encodedEmail = URLEncoder.encode(authResponse.getEmail(), StandardCharsets.UTF_8);
        String encodedNickName = URLEncoder.encode(authResponse.getNickName(), StandardCharsets.UTF_8);
        String encodedProfileImageUrl = URLEncoder.encode(authResponse.getProfileImageUrl(), StandardCharsets.UTF_8);
        String encodedSocialType = URLEncoder.encode(authResponse.getSocialType(), StandardCharsets.UTF_8);

        // 프론트엔드 페이지로 토큰과 함께 리다이렉트
        String RedirectUrl = String.format(
                "%s/?memberId=%s&email=%s&nickName=%s&profileImageUrl=%s&socialType=%s",
                frontendRedirectUrl, encodedMemberId, encodedEmail, encodedNickName,
                encodedProfileImageUrl, encodedSocialType
        );
        response.sendRedirect(RedirectUrl);
    }

}
