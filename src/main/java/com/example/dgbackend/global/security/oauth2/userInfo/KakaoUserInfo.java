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

    @Override
    public String getBirthDate() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String birthYear = (String) kakaoAccount.get("birthyear");
        String birthDay = (String) kakaoAccount.get("birthday");
        return birthYear+birthDay;
    }

    @Override
    public String getPhoneNumber() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String phoneNumber = (String) kakaoAccount.get("phone_number");

        return formatPhoneNumber(phoneNumber);
    }

    @Override
    public String getGender() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return (String) kakaoAccount.get("gender");
    }

    public String formatPhoneNumber(String phoneNumber) {
        String cleanedPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        if (cleanedPhoneNumber.startsWith("82")) {
            cleanedPhoneNumber = "0" + cleanedPhoneNumber.substring(2);
        }

        return cleanedPhoneNumber.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");

    }
}
