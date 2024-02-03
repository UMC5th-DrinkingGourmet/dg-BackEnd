package com.example.dgbackend.domain.recipeimage.dto;

import com.example.dgbackend.domain.recipe.dto.RecipeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeImageResponse {

    @Schema(description = "레시피 정보")
    private RecipeResponse recipe;

    @Schema(description = "레시피 이미지 url 리스트", example = "[\"https://d3h9ln6psucegz.cloudfront.net/images/recipe/recipe_1/recipe_1_1.jpg\"]")
    private List<String> imageUrlList;

    public static RecipeImageResponse toResponse(RecipeResponse recipe, List<String> imageUrlList) {
        return RecipeImageResponse.builder()
                .recipe(recipe)
                .imageUrlList(imageUrlList)
                .build();
    }
}
