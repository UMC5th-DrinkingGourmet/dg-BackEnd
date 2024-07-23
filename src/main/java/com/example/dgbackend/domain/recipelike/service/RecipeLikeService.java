package com.example.dgbackend.domain.recipelike.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipelike.RecipeLike;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeResponse;
import java.util.Optional;

public interface RecipeLikeService {

    RecipeLikeResponse getRecipeLike(Long recipeId, Member member);

    RecipeLikeResponse changeRecipeLike(Long recipeId, Member member);

    RecipeLike createRecipe(Long recipeId, Member member);

    Optional<RecipeLike> getRecipeLikeEntity(Long recipeId, Member member);
}
