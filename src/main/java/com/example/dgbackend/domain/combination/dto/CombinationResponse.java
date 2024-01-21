package com.example.dgbackend.domain.combination.dto;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combinationcomment.domain.CombinationComment;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public class CombinationResponse {

    /**
     * 오늘의 조합 홈 페이지 DTO
     */
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

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationPreviewDTO {
        String title;
        String combinationImageUrl;
        Long likeCount;
        Long commentCount;
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
                .combinationImageUrl(imageUrl)
                .likeCount(combination.getLikeCount())
                .commentCount(combination.getCommentCount())
                .build();
    }

    /**
     * 오늘의 조합 상세 정보 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationDetailDTO {
        CombinationResult combinationResult;
        MemberResponse.MemberResult memberResult;
        CombinationCommentResult combinationCommentResult;
    }

    public static CombinationDetailDTO toCombinationDetailDTO(CombinationResult combinationResult,
                                                              MemberResponse.MemberResult memberResult,
                                                              CombinationCommentResult combinationCommentResult) {
        return CombinationDetailDTO.builder()
                .combinationResult(combinationResult)
                .memberResult(memberResult)
                .combinationCommentResult(combinationCommentResult)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationResult {
        Long combinationId;
        String title;
        String info;
        String content;
    }

    public static CombinationResult toCombinationResult(Combination combination) {
        return CombinationResult.builder()
                .combinationId(combination.getId())
                .title(combination.getTitle())
                .content(combination.getContent())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationCommentResult {
        List<CombinationCommentPreviewDTO> combinationCommentList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationCommentPreviewDTO {
        Long combinationCommentId;
        String content;
        Long parentId;
    }

    public static CombinationCommentPreviewDTO toCommentPreviewDTO(CombinationComment comment) {
        return CombinationCommentPreviewDTO.builder()
                .combinationCommentId(comment.getId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .build();
    }

    public static CombinationCommentResult toCombinationCommentResult(Page<CombinationComment> comments) {

        List<CombinationCommentPreviewDTO> commentPreviewDTOS = comments.stream()
                .map(CombinationResponse::toCommentPreviewDTO)
                .toList();

        return CombinationCommentResult.builder()
                .combinationCommentList(commentPreviewDTOS)
                .listSize(commentPreviewDTOS.size())
                .totalPage(comments.getTotalPages())
                .totalElements(comments.getTotalElements())
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .build();
    }

}
