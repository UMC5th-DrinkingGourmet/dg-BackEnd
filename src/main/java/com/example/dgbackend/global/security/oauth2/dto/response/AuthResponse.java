package com.example.dgbackend.global.security.oauth2.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 응답 DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthResponse {

    @Schema(description = "사용자 인덱스", example = "1")
    private Long memberId;

    @Schema(description = "이메일", example = "member1@naver.com")
    private String email;

    @Schema(description = "사용자 닉네임", example = "nickname1")
    private String nickName;

    @Schema(description = "사용자 프로필 이미지 주소")
    private String profileImageUrl;

    @Schema(description = "소셜 로그인 유형", example = "kakao")
    private String socialType;
}
