package com.example.dgbackend.domain.combinationimage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CombinationImageResponse {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationImageResult {
        List<String> combinationImageList;
    }

    public static CombinationImageResult toCombinationImageResult(List<String> imageUrls) {
        return CombinationImageResult.builder()
                .combinationImageList(imageUrls)
                .build();
    }
}
