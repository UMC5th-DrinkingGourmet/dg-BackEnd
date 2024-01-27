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
     * 오늘의 조합에 작성된 댓글 페이징 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CommentPreViewResult {
        List<CommentResult> combinationCommentList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    public static CommentPreViewResult toCommentPreViewResult(Page<CombinationComment> comments) {

        List<CommentResult> commentPreviewList = comments.stream()
                .filter(commet -> commet.getParentComment() == null)
                .map(CombinationCommentResponse::toCommentResult)
                .toList();

        return CommentPreViewResult.builder()
                .combinationCommentList(commentPreviewList)
                .listSize(commentPreviewList.size())
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
        private Integer childCount;
        private List<CommentResult> childComments = new ArrayList<>();
    }

    public static CommentResult toCommentResult(CombinationComment combinationComment) {

        return CommentResult.builder()
                .id(combinationComment.getId())
                .content(combinationComment.getContent())
                .memberName(combinationComment.getMember().getName())
                .updatedAt(combinationComment.getUpdatedAt())
                .childCount(getChildCount(combinationComment))
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

    private static Integer getChildCount(CombinationComment combinationComment) {

        return Optional.ofNullable(combinationComment.getChildComments())
                .map(childComments -> (int) childComments.stream()
                        .filter(CombinationComment::isState)
                        .count())
                .orElse(null);
    }

    /**
     * 작성, 수정, 삭제시 응답 DTO
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CommentProcResult {
        Long commentId;
        LocalDateTime createdAt;
    }

    public static CombinationCommentResponse.CommentProcResult toCommentProcResult(Long commentId) {
        return CommentProcResult.builder()
                .commentId(commentId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
