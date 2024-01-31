package com.example.dgbackend.domain.recipe.dto;

import com.example.dgbackend.domain.recipe.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {

    @NotNull
    @Schema(description = "레시피 Id", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "레시피 이름", example = "김치찌개")
    private String name;

    @NotNull
    @Schema(description = "레시피 소개", example = "맛있는 김치찌개")
    private String info;

    @NotNull
    @Schema(description = "조리시간", example = "30분")
    private String cookingTime;

    @NotNull
    @Schema(description = "칼로리", example = "500kcal")
    private String calorie;

    @NotNull
    @Schema(description = "좋아요 수", example = "10")
    private Long likeCount;

    @NotNull
    @Schema(description = "댓글 수", example = "5")
    private Long commentCount;

    @NotNull
    @Schema(description = "재료", example = "김치, 돼지고기, 두부")
    private String ingredient;

    @NotNull
    @Schema(description = "레시피 순서", example = "1. 김치를 넣는다. 2. 물을 넣는다.")
    private String recipeInstruction;

    @Schema(description = "추천 조합", example = "김치찌개와 참이슬")
    private String recommendCombination; //추천받은 조합

    @Schema(description = "존재 여부", example = "true")
    private boolean state = true; //true : 존재, false : 삭제

    @Schema(description = "작성자 이름", example = "김동규")
    private String memberName;

    public static RecipeResponse toResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .info(recipe.getInfo())
                .cookingTime(recipe.getCookingTime())
                .calorie(recipe.getCalorie())
                .likeCount(recipe.getLikeCount())
                .commentCount(recipe.getCommentCount())
                .ingredient(recipe.getIngredient())
                .recipeInstruction(recipe.getRecipeInstruction())
                .recommendCombination(recipe.getRecommendCombination())
                .state(recipe.isState())
                .memberName(recipe.getMember().getName())
                .build();
    }

}
