package com.example.dgbackend.domain.combinationcomment.dto;

import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
//                .parentId(comment.getParentId())
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

    /**
     * 오늘의 조합 댓글 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CommentResult {

        private Long id;
        private String content;
        private String memberName;
        private LocalDateTime updatedAt; // 댓글 생성 및 수정 시간
        private List<CommentResult> childComments = new ArrayList<>();
    }

    public static CommentResult toCommentResult(CombinationComment combinationComment) {

        return CommentResult.builder()
                .id(combinationComment.getId())
                .content(combinationComment.getContent())
                .memberName(combinationComment.getMember().getName())
                .updatedAt(combinationComment.getUpdatedAt())
                .childComments(getChildComments(combinationComment))
                .build();
    }

    private static List<CommentResult> getChildComments(CombinationComment combinationComment) {

        return Optional.ofNullable(combinationComment.getChildComments())
                .orElse(new ArrayList<>()) // 자식 댓글 없는 경우
                .stream()
                .filter(CombinationComment::isState) // 존재하는 댓글만 필터링
                .map(CombinationCommentResponse::toCommentResult)
                .toList();
    }

}
