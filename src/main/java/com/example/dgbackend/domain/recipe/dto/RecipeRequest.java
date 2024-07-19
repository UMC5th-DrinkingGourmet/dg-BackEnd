package com.example.dgbackend.domain.recipe.dto;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {

    @NotNull
    @Schema(description = "레시피 이름", example = "김치찌개")
    private String title;

    @NotNull
    @Schema(description = "조리시간", example = "30분")
    private String cookingTime;

    @NotNull
    @Schema(description = "칼로리", example = "500kcal")
    private String calorie;

    @NotNull
    @Schema(description = "재료", example = "김치, 돼지고기, 두부")
    private String ingredient;

    @NotNull
    @Schema(description = "레시피 순서", example = "1. 김치를 넣는다. 2. 물을 넣는다.")
    private String recipeInstruction;

    @Schema(description = "추천 조합", example = "김치찌개와 참이슬")
    private String recommendCombination;

    @Schema(description = "해시태그 리스트", example = "[\"김치찌개\", \"참이슬\"]")
    private List<String> hashTagNameList;

    public static Recipe toEntity(RecipeRequest recipeRequest, Member member) {
        return Recipe.builder()
                .title(recipeRequest.getTitle())
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
