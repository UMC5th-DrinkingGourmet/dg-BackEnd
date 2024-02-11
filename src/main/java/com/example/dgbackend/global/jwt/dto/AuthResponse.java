package com.example.dgbackend.global.jwt.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String provider;
    private String nickName;
    private LocalDateTime createdAt;

    public static AuthResponse toAuthResponse(String provider, String nickName) {

        return AuthResponse.builder()
            .provider(provider)
            .nickName(nickName)
            .createdAt(LocalDateTime.now())
            .build();
    }
}