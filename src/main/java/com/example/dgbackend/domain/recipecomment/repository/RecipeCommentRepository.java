package com.example.dgbackend.domain.recipecomment.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findAllByRecipe(Recipe recipe);
    Page<RecipeComment> findAllByRecipe(Recipe recipe, Pageable pageable);

    @Query("SELECT r FROM RecipeComment r WHERE r.recipe = :recipe AND (r.state = 'TRUE' OR r.state = 'REPORTED')")
    Page<RecipeComment> findByRecipeAndParentCommentIsNullAndStateIsTrueOrReported(Recipe recipe, Pageable pageable);

    Optional<RecipeComment> findByIdAndStateIsTrue(Long id);
}
