package com.example.dgbackend.domain.recipe.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.dto.RecipeRequest;
import com.example.dgbackend.domain.recipe.dto.RecipeResponse;

public interface RecipeService {

    RecipeResponse.RecipeResponseList getExistRecipes(int page, Member loginMember);


    RecipeResponse getRecipeDetail(Long id, Member member);

    RecipeResponse createRecipe(RecipeRequest recipeRequest, Member loginMember);

    RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest, Member loginMember);

    String deleteRecipe(Long id, Member loginMember);


    //레시피 이름과 회원 이름으로 레시피 탐색
    Recipe getRecipe(Long id);

    //레시피가 삭제된 경우 예외처리
    Recipe isDelete(Recipe recipe);

    void isAlreadyCreate(String RecipeName, String memberName);

    RecipeResponse.RecipeMyPageList getRecipeMyPageList(Member loginMember, Integer Page);

    RecipeResponse.RecipeMyPageList getRecipeLikeList(Member loginMember, Integer Page);

    RecipeResponse.RecipeResponseList findRecipesByKeyword(Integer page, String keyword,
        Member loginMember);

    RecipeResponse getRecipeDetailResponse(Recipe recipes, Member loginMember);

    void deleteRecipeWithRelations(Recipe recipe);
}
