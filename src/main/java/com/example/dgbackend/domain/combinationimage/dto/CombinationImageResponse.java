package com.example.dgbackend.domain.combinationimage.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
