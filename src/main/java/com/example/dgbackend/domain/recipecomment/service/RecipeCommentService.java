package com.example.dgbackend.domain.recipecomment.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentRequest;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentResponse;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentVO;

public interface RecipeCommentService {

    RecipeCommentResponse.RecipeCommentResponseList getRecipeComment(Long recipeId, int page,
        Member loginMember);

    RecipeCommentResponse saveRecipeComment(RecipeCommentVO paramVO, Member loginMember);

    RecipeComment getEntity(RecipeCommentVO paramVO, Member loginMember);

    RecipeComment getParentEntityById(Long id);

    RecipeComment getEntityById(Long id);

    RecipeCommentResponse updateRecipeComment(RecipeCommentRequest.Patch requestDTO,
        Member loginMember);

    RecipeCommentResponse deleteRecipeComment(Long recipeCommentId, Member loginMember);

    void deleteAllRecipeComment(Recipe recipe);
}
