package com.example.dgbackend.domain.recipeimage.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {

    List<RecipeImage> findAllByRecipeId(Long recipeId);

    List<RecipeImage> findAllByRecipe(Recipe recipe);

    Optional<RecipeImage> findByImageUrl(String imageUrl);

    void deleteAllByRecipe(Recipe recipe);

    @Modifying
    @Query("DELETE from RecipeImage ri where ri.imageUrl = :imageUrl")
    void deleteByImageUrl(String imageUrl);

}
