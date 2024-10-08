package com.example.dgbackend.domain.combination.dto;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recommend.Recommend;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class CombinationRequest {

    @Getter
    @Schema(name = "오늘의 조합 작성 요청 DTO")
    public static class WriteCombination {

        String title;
        String content;
        Long recommendId;
        List<String> hashTagNameList;
        List<String> combinationImageList;
    }

    public static Combination createCombination(Member member, String title, String content, Recommend recommend,
        String... imageUrls) {
        Combination combination = Combination.builder()
            .member(member)
            .title(title)
            .content(content)
            .recommend(recommend)
            .combinationImages(new ArrayList<>())
            .build();

        for (String imageUrl : imageUrls) {
            CombinationImage combinationImage = CombinationImage.builder()
                .imageUrl(imageUrl)
                .build();
            combination.addCombinationImage(combinationImage);
        }
        return combination;
    }
}
