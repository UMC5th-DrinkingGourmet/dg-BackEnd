package com.example.dgbackend.global.security.oauth2.userInfo;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    @Override
    public String getProfile() {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return (String) kakaoProfile.get("profile_image_url");
    }

    @Override
    public String getEmail() {
        return (String) ((Map)attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map)attributes.get("kakao_account")).get("name");
    }

    @Override
    public String getNickname() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return (String) kakaoProfile.get("nickname");
    }
}
