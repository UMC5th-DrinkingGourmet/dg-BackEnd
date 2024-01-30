package com.example.dgbackend.global.security.oauth2.userInfo;

public interface OAuth2UserInfo {
    String getProfile();

    String getEmail();

    String getName();

    String getNickname();

    String getBirthDate();

    String getPhoneNumber();

    String getGender();

}
