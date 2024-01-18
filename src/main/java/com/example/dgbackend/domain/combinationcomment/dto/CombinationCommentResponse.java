package com.example.dgbackend.domain.combinationcomment.dto;

import com.example.dgbackend.domain.combinationcomment.domain.CombinationComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public class CombinationCommentResponse {

    /**
     * 오늘의 조합에 작성된 댓글 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CombinationCommentResult {
        List<CombinationCommentPreviewResult> combinationCommentList;
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
    public static class CombinationCommentPreviewResult {
        Long combinationCommentId;
        String content;
        Long parentId;
    }

    public static CombinationCommentPreviewResult toCommentPreviewResult(CombinationComment comment) {
        return CombinationCommentPreviewResult.builder()
                .combinationCommentId(comment.getId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .build();
    }

    public static CombinationCommentResult toCombinationCommentResult(Page<CombinationComment> comments) {

        List<CombinationCommentPreviewResult> commentPreviewDTOS = comments.stream()
                .map(CombinationCommentResponse::toCommentPreviewResult)
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
