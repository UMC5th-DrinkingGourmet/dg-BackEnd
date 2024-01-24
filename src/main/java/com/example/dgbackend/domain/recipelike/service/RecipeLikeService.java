package com.example.dgbackend.domain.recipelike.service;

import com.example.dgbackend.domain.recipelike.RecipeLike;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeResponse;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeVO;

import java.util.Optional;

public interface RecipeLikeService {

    RecipeLikeResponse getRecipeLike(RecipeLikeVO recipeLikeVO);

    RecipeLikeResponse changeRecipeLike(RecipeLikeVO recipeLikeVO);

    RecipeLike createRecipe(RecipeLikeVO recipeLikeVO);

    Optional<RecipeLike> getRecipeLikeEntity(RecipeLikeVO recipeLikeVO);

}
