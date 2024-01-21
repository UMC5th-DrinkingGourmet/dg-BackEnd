package com.example.dgbackend.domain.combinationcomment.dto;

import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public class CombinationCommentResponse {

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
                .map(CombinationCommentResponse::toCommentPreviewDTO)
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
