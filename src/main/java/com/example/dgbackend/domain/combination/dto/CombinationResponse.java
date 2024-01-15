package com.example.dgbackend.domain.combination.dto;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public class CombinationResponse {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationPreviewDTO {
        private String title;
        private String info;
        private String combinationImageUrl;
        private Long likeCount;
        private Long commentCount;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationPreviewDTOList {
        List<CombinationPreviewDTO> combinationList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    // Page<Combination> -> Page<CombinationPreviewDTO> 로 변환
    public static CombinationPreviewDTOList toCombinationPreviewDTOList(Page<Combination> combinations) {

        List<CombinationPreviewDTO> combinationPreviewDTOS = combinations.stream()
                .map(CombinationResponse::toCombinationPreviewDTO)
                .toList();

        return CombinationPreviewDTOList.builder()
                .combinationList(combinationPreviewDTOS)
                .listSize(combinationPreviewDTOS.size())
                .totalPage(combinations.getTotalPages())
                .totalElements(combinations.getTotalElements())
                .isFirst(combinations.isFirst())
                .isLast(combinations.isLast())
                .build();
    }

    // Combination -> CombinationPreviewDTO로 변환
    public static CombinationPreviewDTO toCombinationPreviewDTO(Combination combination) {
        // TODO: 대표 이지미 정하기
        String imageUrl = combination.getCombinationImages()
                .stream()
                .findFirst()
                .map(CombinationImage::getImageUrl)
                .orElse(null);

        return CombinationPreviewDTO.builder()
                .title(combination.getTitle())
                .info(combination.getInfo())
                .combinationImageUrl(imageUrl)
                .likeCount(combination.getLikeCount())
                .commentCount(combination.getCommentCount())
                .build();
    }
}
