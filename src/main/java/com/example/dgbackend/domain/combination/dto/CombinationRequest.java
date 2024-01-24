package com.example.dgbackend.domain.combination.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

public class CombinationRequest {

    @Getter
    @Schema(name = "오늘의 조합 작성 요청 DTO")
    public static class WriteCombination {

        String title;
        String content;
        List<String> hashTagNameList;
        List<String> combinationImageList;
    }

}
