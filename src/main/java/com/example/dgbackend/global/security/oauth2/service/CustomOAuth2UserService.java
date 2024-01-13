package com.example.dgbackend.global.security.oauth2.service;

import com.example.dgbackend.domain.enums.Role;
import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.security.oauth2.principal.CustomOAuth2User;
import com.example.dgbackend.global.security.oauth2.userInfo.OAuth2UserInfo;
import com.example.dgbackend.global.security.oauth2.userInfo.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        log.info("------------------ getAttributes : {}", oAuth2User.getAttributes());

        String provider = request.getClientRegistration().getRegistrationId();

        // SocialType Enum으로 변환
        SocialType socialType = SocialType.valueOf(provider.toUpperCase());

        // 어떤 소셜 로그인인지 구분
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuthUserInfo(provider, oAuth2User.getAttributes());

        // 회원 가입 유무 확인
        Optional<Member> member = memberRepository.findByEmailAndSocialType(oAuth2UserInfo.getEmail(), socialType);

        // 회원이 아닌 경우 회원가입
        if (member.isEmpty()) {
            Member newMember = Member.builder()
                    .nickName(oAuth2UserInfo.getNickname())
                    .email(oAuth2UserInfo.getEmail())
                    .role(Role.MEMBER)
                    .profileImageUrl(oAuth2UserInfo.getProfile())
                    .state(true)
                    .socialType(socialType)
                    .build();

            memberRepository.save(newMember);

            return new CustomOAuth2User(newMember, oAuth2User.getAttributes());
        }
        return new CustomOAuth2User(member.get(), oAuth2User.getAttributes());
    }
}
