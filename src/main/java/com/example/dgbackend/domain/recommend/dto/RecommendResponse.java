package com.example.dgbackend.domain.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecommendResponse {

    /*
    주류 추천 응답 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RecommendResponseDTO {
        String foodName;
        String drinkName;
        String recommendReason;
    }

    /*
    주류 추천 응답 이미지 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RecommendImageResponseDTO {
        String imageUrl;
    }

    @Builder
    @Getter
    public static class GPTResponse{
        private int index;
        private RecommendRequest.GPTMessage message;
    }
}
