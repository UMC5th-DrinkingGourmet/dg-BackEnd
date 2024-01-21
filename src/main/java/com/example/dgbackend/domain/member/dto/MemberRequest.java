package com.example.dgbackend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
