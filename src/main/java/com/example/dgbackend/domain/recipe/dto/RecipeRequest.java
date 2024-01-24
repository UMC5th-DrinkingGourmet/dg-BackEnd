package com.example.dgbackend.domain.recipe.dto;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {

    @NotNull
    private String name;

    @NotNull
    private String info;

    @NotNull
    private String cookingTime;

    @NotNull
    private String calorie;

    @NotNull
    private String ingredient;

    @NotNull
    private String recipeInstruction;

    private String recommendCombination;

    public static Recipe toEntity(RecipeRequest recipeRequest, Member member) {
        return Recipe.builder()
                .name(recipeRequest.getName())
                .info(recipeRequest.getInfo())
                .cookingTime(recipeRequest.getCookingTime())
                .calorie(recipeRequest.getCalorie())
                .likeCount(0L)
                .commentCount(0L)
                .ingredient(recipeRequest.getIngredient())
                .recipeInstruction(recipeRequest.getRecipeInstruction())
                .recommendCombination(recipeRequest.getRecommendCombination())
                .member(member)
                .build();
    }

}
