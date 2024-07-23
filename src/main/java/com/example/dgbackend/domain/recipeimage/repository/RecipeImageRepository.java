package com.example.dgbackend.domain.recipeimage.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {

    List<RecipeImage> findAllByRecipeId(Long recipeId);

    List<RecipeImage> findAllByRecipe(Recipe recipe);

    Optional<RecipeImage> findByImageUrl(String imageUrl);
}
