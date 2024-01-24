package com.example.dgbackend.domain.recommend.dto;


import com.example.dgbackend.domain.recommend.Recommend;
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
        String foodName;            //음식 이름
        String drinkName;           //술 이름
        String recommendReason;     //추천 이유
        String imageUrl;            //이미지 S3 URL
    }

    @Builder
    @Getter
    public static class GPTResponse{
        private int index;
        private RecommendRequest.GPTMessage message;
    }

    public static class RecommendResult {
        String drinkName;
        String drinkInfo;
        String imageUrl;
    }

    public static RecommendResult toRecommendResult(Recommend recommend) {
        return RecommendResult.builder()
                .drinkName(recommend.getDrinkName())
                .drinkInfo(recommend.getDrinkInfo())
                .imageUrl(recommend.getImageUrl())
                .build();
    }
}
