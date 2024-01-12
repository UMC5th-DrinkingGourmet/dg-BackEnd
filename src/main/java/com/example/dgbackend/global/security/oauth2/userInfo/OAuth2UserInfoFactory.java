package com.example.dgbackend.global.security.oauth2.userInfo;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuthUserInfo(String provider, Map<String, Object> attributes) {

        if (provider.equals("kakao")) {
            log.info("----------------------------- 카카오 로그인 요청");
            return new KakaoUserInfo(attributes);
        } else if (provider.equals("naver")) {
            log.info("----------------------------- 네이버 로그인 요청");
//            return new NaverUserInfo(attributes);
        } else if (provider.equals("apple")) {
            log.info("----------------------------- 애플 로그인 요청");
//            return new AppleUserInfo(attributes);
        }

        return null;
    }
}
