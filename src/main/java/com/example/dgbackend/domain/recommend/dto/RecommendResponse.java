package com.example.dgbackend.domain.recommend.dto;


import com.example.dgbackend.domain.recommend.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RecommendResponse {

    /*
    주류 추천 응답 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RecommendResponseDTO {
        Long recommendID;           //추천 ID
        String foodName;            //음식 이름
        String drinkName;           //술 이름
        String recommendReason;     //추천 이유
        String imageUrl;            //이미지 S3 URL
    }

    @Builder
    @Getter
    public static class GPTResponse {
        private int index;
        private RecommendRequest.GPTMessage message;
    }

    @Getter
    @Builder
    public static class RecommendListResult {
        List<RecommendResponseDTO> recommendResponseDTOList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    public static RecommendResponseDTO toRecommendResult(Recommend recommend) {
        return RecommendResponseDTO.builder()
                .recommendID(recommend.getId())
                .foodName(recommend.getFoodName())
                .drinkName(recommend.getDrinkName())
                .recommendReason(recommend.getDrinkInfo())
                .imageUrl(recommend.getImageUrl())
                .build();
    }
}
