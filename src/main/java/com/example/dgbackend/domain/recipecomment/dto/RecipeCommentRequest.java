package com.example.dgbackend.domain.recipecomment.dto;

import com.example.dgbackend.domain.enums.State;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


public class RecipeCommentRequest {

    public static RecipeComment toEntity(Member member, Recipe recipe, String comment,
        RecipeComment parentComment) {
        return RecipeComment.builder()
            .content(comment)
            .state(State.TRUE)
            .member(member)
            .parentComment(parentComment)
            .recipe(recipe)
            .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Post {

        @NotNull
        @Schema(description = "댓글 내용", example = "맛있어요")
        private String content;

        @NotNull
        @Schema(description = "부모 댓글 Id\n0은 댓글, 나머지는 부모 ID", example = "0")
        private Long parentId;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Patch {

        @NotNull
        @Schema(description = "댓글 내용", example = "맛있어요")
        private String content;

        @NotNull
        @Schema(description = "댓글 Id", example = "1")
        private Long recipeCommentId;

    }

}
