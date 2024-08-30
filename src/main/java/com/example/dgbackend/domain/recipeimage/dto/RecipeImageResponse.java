package com.example.dgbackend.domain.recipeimage.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecipeImageResponse {


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RecipeImageResult {

        List<String> recipeImageList;
    }

    public static RecipeImageResult toRecipeImageResult(List<String> imageUrls) {
        return RecipeImageResult.builder()
            .recipeImageList(imageUrls)
            .build();
    }
}
