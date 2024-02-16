package com.example.dgbackend.domain.recipe_hashtag.repository;

import com.example.dgbackend.domain.hashtag.HashTag;
import com.example.dgbackend.domain.recipe_hashtag.RecipeHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeHashTagRepository extends JpaRepository<RecipeHashTag, Long> {

    Optional<RecipeHashTag> findByRecipe_IdAndHashtag_Id(Long recipeId, Long hashtagId);

    void deleteByRecipe_Id(Long recipeId);

}
