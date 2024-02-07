package com.example.dgbackend.domain.recipecomment.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findAllByRecipe(Recipe recipe);
    Page<RecipeComment> findAllByRecipe(Recipe recipe, Pageable pageable);
}
