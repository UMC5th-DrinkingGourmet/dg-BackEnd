package com.example.dgbackend.global.security.oauth2.principal;

import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.domain.member.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
public class CustomOAuth2User implements OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    // OAuth2 로그인 생성자
    public CustomOAuth2User(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER"));
    }

    @Override
    public String getName() {
        if (member.getSocialType().equals(SocialType.KAKAO)) {
            return ((Map<?, ?>) attributes.get("properties")).get("nickname").toString();
        }

        if(member.getSocialType().equals(SocialType.NAVER)) {
            return ((Map<?, ?>) attributes.get("response")).get("nickname").toString();
        }

        return null;
    }
}
