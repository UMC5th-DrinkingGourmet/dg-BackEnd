package com.example.dgbackend.global.jwt.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthResult {
        private String provider;
        private String nickName;
        private Long memberId;
        private boolean newMember;
        private LocalDateTime createdAt;
    }

    public static AuthResult toAuthResult(String provider, String nickName, boolean newMember, Long memberId) {

        return AuthResult.builder()
            .provider(provider)
            .nickName(nickName)
            .memberId(memberId)
            .newMember(newMember)
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IsSignedUpResult {
        private Boolean isSignedUp;
    }

    public static IsSignedUpResult toIsSignedUpResult(Boolean isSignedUp) {

        return IsSignedUpResult.builder()
                .isSignedUp(isSignedUp)
                .build();
    }
}