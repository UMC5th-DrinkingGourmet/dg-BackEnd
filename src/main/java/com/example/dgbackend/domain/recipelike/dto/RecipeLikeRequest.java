package com.example.dgbackend.domain.recipelike.dto;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipelike.RecipeLike;

public class RecipeLikeRequest {

    public static RecipeLike toEntity(Recipe recipe, Member member) {
        return RecipeLike.builder()
                .recipe(recipe)
                .member(member)
                .state(true)
                .build();
    }

}
