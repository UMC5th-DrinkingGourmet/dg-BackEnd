package com.example.dgbackend.global.security.oauth2.userInfo;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class NaverUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return (String) response.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return (String) response.get("name");
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return (String) response.get("nickname");
    }

    @Override
    public String getProfile() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return (String) response.get("profile_image");
    }

    @Override
    public String getBirthDate() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        String birthDate = (String) response.get("birthday");
        String birthYear = (String) response.get("birthyear");

        return birthYear + "-" + birthDate;
    }

    @Override
    public String getPhoneNumber() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return (String) response.get("mobile");
    }

    @Override
    public String getGender() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String gender = (String) response.get("gender");

        if (gender.equals("F")) {
            return "FEMALE";
        } else
            return "MALE";
    }
}
