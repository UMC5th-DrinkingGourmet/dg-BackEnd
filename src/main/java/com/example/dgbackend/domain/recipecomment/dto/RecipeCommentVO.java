package com.example.dgbackend.domain.recipecomment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecipeCommentVO {

    private String memberName;
    private Long recipeId;
    private String content;
    private Long parentId;


    public static RecipeCommentVO of(RecipeCommentRequest.Post recipeCommentRequest, Long recipeId, String memberName) {
        return RecipeCommentVO.builder()
                .memberName(memberName)
                .recipeId(recipeId)
                .content(recipeCommentRequest.getContent())
                .parentId(recipeCommentRequest.getParentId())
                .build();
    }

}
