package com.example.dgbackend.domain.recipecomment.dto;

import com.example.dgbackend.domain.recipecomment.RecipeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Schema(description = "작성자 닉니임 ", example = "mason")
    private String memberNickName;

    @Schema(description = "작성일", example = "2021-08-01 12:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정일", example = "2021-08-01 12:00:00")
    private LocalDateTime updatedDate;

    @Builder.Default
    @Schema(description = "자식 댓글 목록", example = "[]")
    private List<RecipeCommentResponse> childCommentList = new ArrayList<>();

    public static RecipeCommentResponse toResponse(RecipeComment recipeComment) {

        //부모 Comment가 null이면 0으로 설정, null이 아니면 부모 Comment의 id값으로 설정
        Long parentId = Optional.ofNullable(recipeComment.getParentComment())
                .map(RecipeComment::getId)
                .orElse(0L);

        return RecipeCommentResponse.builder()
                .id(recipeComment.getId())
                .content(recipeComment.getContent())
                .childCommentList(getList(recipeComment))
                .memberNickName(recipeComment.getMember().getNickName())
                .createdDate(recipeComment.getCreatedAt())
                .updatedDate(recipeComment.getUpdatedAt())
                .build();
    }

    private static List<RecipeCommentResponse> getList(RecipeComment recipeComment) {
        return Optional.ofNullable(recipeComment.getChildCommentList())

                //자식없을 때
                .orElse(new ArrayList<>())

                //자식있을 때, 존재하는 것 만 반환
                .stream()
                .filter(RecipeComment::isState)
                .map(RecipeCommentResponse::toResponse).toList();
    }

}
