package com.example.dgbackend.domain.recipecomment.service;

import com.example.dgbackend.domain.recipecomment.RecipeComment;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentRequest;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentResponse;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentVO;

import java.util.List;

public interface RecipeCommentService {

    List<RecipeCommentResponse> getRecipeComment(Long recipeId);

    RecipeCommentResponse saveRecipeComment(RecipeCommentVO paramVO);

    RecipeComment getEntity(RecipeCommentVO paramVO);

    RecipeComment getParentEntityById(Long id);

    RecipeComment getEntityById(Long id);

    RecipeCommentResponse updateRecipeComment(RecipeCommentRequest.Patch requestDTO);

    RecipeCommentResponse deleteRecipeComment(Long recipeCommentId);

    boolean deleteAllRecipeComment(Long memberId);
}
