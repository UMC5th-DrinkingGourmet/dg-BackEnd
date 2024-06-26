package com.example.dgbackend.domain.recipecomment.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentRequest;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentResponse;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentVO;

import java.util.List;

public interface RecipeCommentService {

    RecipeCommentResponse.RecipeCommentResponseList getRecipeComment(Long recipeId, int page, Member loginMember);

    RecipeCommentResponse saveRecipeComment(RecipeCommentVO paramVO);

    RecipeComment getEntity(RecipeCommentVO paramVO);

    RecipeComment getParentEntityById(Long id);

    RecipeComment getEntityById(Long id);

    RecipeCommentResponse updateRecipeComment(RecipeCommentRequest.Patch requestDTO);

    RecipeCommentResponse deleteRecipeComment(Long recipeCommentId);
}
