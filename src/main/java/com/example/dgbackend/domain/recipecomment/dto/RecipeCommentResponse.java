package com.example.dgbackend.domain.recipecomment.dto;

import com.example.dgbackend.domain.enums.State;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeCommentResponse {

    @NotNull
    @Schema(description = "댓글 Id", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "댓글 내용", example = "맛있어요")
    private String content;

    @Schema(description = "작성자 정보")
    private MemberResponse.MemberResult member;

    @Schema(description = "작성일", example = "2021-08-01 12:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정일", example = "2021-08-01 12:00:00")
    private LocalDateTime updatedDate;

    @Builder.Default
    @Schema(description = "자식 댓글 목록", example = "[]")
    private List<RecipeCommentResponse> childCommentList = new ArrayList<>();

    @Schema(description = "자식 댓글 수", example = "5")
    private int childCommentCount = 0;

    @Schema(description = "댓글 상태", example = "TRUE")
    private State state;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RecipeCommentResponseList {

        private List<RecipeCommentResponse> commentList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    public static RecipeCommentResponseList toResponseList(Page<RecipeComment> recipeCommentList) {
        return RecipeCommentResponseList.builder()
                .commentList(recipeCommentList.stream().map(RecipeCommentResponse::toResponse).toList())
                .listSize(recipeCommentList.getNumberOfElements())
                .totalPage(recipeCommentList.getTotalPages())
                .totalElements(recipeCommentList.getTotalElements())
                .isFirst(recipeCommentList.isFirst())
                .isLast(recipeCommentList.isLast())
                .build();
    }


    public static RecipeCommentResponse toResponse(RecipeComment recipeComment) {

        //부모 Comment가 null이면 0으로 설정, null이 아니면 부모 Comment의 id값으로 설정
        Long parentId = Optional.ofNullable(recipeComment.getParentComment())
                .map(RecipeComment::getId)
                .orElse(0L);

        int childCommentCount = Optional.ofNullable(recipeComment.getChildCommentList())
                .orElse(new ArrayList<>())
                .size();

        return RecipeCommentResponse.builder()
                .id(recipeComment.getId())
                .content(recipeComment.getContent())
                .childCommentList(getList(recipeComment))
                .member(MemberResponse.toMemberResult(recipeComment.getMember()))
                .createdDate(recipeComment.getCreatedAt())
                .updatedDate(recipeComment.getUpdatedAt())
                .childCommentCount(childCommentCount)
                .state(recipeComment.getState())
                .build();
    }

    private static List<RecipeCommentResponse> getList(RecipeComment recipeComment) {
        return Optional.ofNullable(recipeComment.getChildCommentList())

                //자식없을 때
                .orElse(new ArrayList<>())

                //자식있을 때, 존재하는 것 만 반환
                .stream()
                .filter(comment -> comment.getState().equals(State.TRUE))
                .map(RecipeCommentResponse::toResponse).toList();
    }

}
