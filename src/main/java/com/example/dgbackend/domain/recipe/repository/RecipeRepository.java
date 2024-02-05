package com.example.dgbackend.domain.recipe.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByState(boolean state);

    List<Recipe> findAllByNameAndMember_Name(String name, String memberName);

    List<Recipe> findRecipesByNameContaining(String keyword);
}
