package com.example.dgbackend.domain.member.dto;

import com.example.dgbackend.domain.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class MemberRequest {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RecommendInfoDTO {
        // 주류 추천 정보 입력 DTO
        private String preferredAlcoholType;  //선호 주종
        private String preferredAlcoholDegree; // 선호 도수
        private String drinkingLimit; //주량
        private String drinkingTimes; // 음주 횟수
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PatchMember {
        private String name;
        private String nickName;
        private MultipartFile profileImage;
        private String birthDate;
        private String phoneNumber;
        @Enumerated(EnumType.STRING)
        private Gender gender;
    }
}
