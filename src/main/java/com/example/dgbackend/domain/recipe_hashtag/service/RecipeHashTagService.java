package com.example.dgbackend.domain.recipe_hashtag.service;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe_hashtag.RecipeHashTag;
import java.util.List;

public interface RecipeHashTagService {

    List<RecipeHashTag> uploadRecipeHashTag(Recipe recipe, List<String> hashTagName);

    void deleteRecipeHashTag(Long recipeId);

    void deleteAllRecipeHashTag(Recipe recipe);

}
