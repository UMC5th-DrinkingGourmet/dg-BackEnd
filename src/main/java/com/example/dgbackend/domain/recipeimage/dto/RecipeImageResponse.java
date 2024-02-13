package com.example.dgbackend.domain.recipeimage.dto;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
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


    @Schema(description = "레시피 이미지 url 리스트", example = "[\"https://d3h9ln6psucegz.cloudfront.net/images/recipe/recipe_1/recipe_1_1.jpg\"]")
    private List<String> imageUrlList;

    public static RecipeImageResponse toURLResponse(Recipe recipe) {
        return RecipeImageResponse.builder()
                .imageUrlList(RecipeImageResponse.toStringResponse(recipe.getRecipeImageList()))
                .build();
    }

    public static List<String> toStringResponse(List<RecipeImage> recipeList) {
        return recipeList.stream()
                .map(RecipeImage::getImageUrl)
                .toList();
    }

}
