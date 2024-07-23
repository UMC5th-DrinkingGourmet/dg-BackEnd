package com.example.dgbackend.domain.recipeimage.dto;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecipeImageRequest {

    public static RecipeImage toEntity(Recipe recipe, String imageUrl) {
        return RecipeImage.builder()
            .recipe(recipe)
            .imageUrl(imageUrl)
            .build();
    }

}
