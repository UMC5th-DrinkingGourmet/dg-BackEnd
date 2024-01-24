package com.example.dgbackend.domain.recipecomment.dto;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


public class RecipeCommentRequest {

    public static RecipeComment toEntity(Member member, Recipe recipe, String comment, RecipeComment parentComment) {
        return RecipeComment.builder()
                .content(comment)
                .state(true)
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
        private String content;

        @NotNull
        private Long parentId;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Patch {

        @NotNull
        private String content;

        @NotNull
        private Long recipeCommentId;

    }

}
