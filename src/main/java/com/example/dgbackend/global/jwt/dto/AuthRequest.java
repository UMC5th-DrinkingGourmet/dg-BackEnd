package com.example.dgbackend.global.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequest {

    @Schema(name = "사용자 정보")
    @Getter
    public static class AuthDTO {
        private String name;
        private String profileImage;
        private String email;
        private String nickName;
        private String birthDate;
        private String phoneNumber;
        private String gender;
        private String provider;
        private String providerId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SignedUpDTO {
        private String provider;
        private String providerId;
    }
}
