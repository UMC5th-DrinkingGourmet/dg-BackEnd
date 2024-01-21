package com.example.dgbackend.domain.recommend.dto;

import com.example.dgbackend.domain.recommend.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecommendResponse {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
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
