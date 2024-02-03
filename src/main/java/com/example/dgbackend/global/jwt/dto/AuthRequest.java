package com.example.dgbackend.global.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(name = "사용자 정보")
@Getter
public class AuthRequest {

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
