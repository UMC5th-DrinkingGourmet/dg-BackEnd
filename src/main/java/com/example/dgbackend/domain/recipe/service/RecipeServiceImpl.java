package com.example.dgbackend.domain.recipe.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.service.MemberService;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.dto.RecipeRequest;
import com.example.dgbackend.domain.recipe.dto.RecipeResponse;
import com.example.dgbackend.domain.recipe.repository.RecipeRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.dgbackend.domain.recipe.dto.RecipeResponse.toRecipeMyPageList;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final MemberService memberService;

    @Override
    public List<RecipeResponse> getExistRecipes() {
        return recipeRepository.findAllByState(true).stream()
                .map(RecipeResponse::toResponse)
                .toList();
    }

    @Override
    public RecipeResponse getRecipeDetail(Long id) {
        Recipe recipe = getRecipe(id);
        return RecipeResponse.toResponse(recipe);
    }

    @Override
    public RecipeResponse createRecipe(RecipeRequest recipeRequest, Member member) {

        Member memberEntity = memberService.findMemberByName(member.getName());
        isAlreadyCreate(recipeRequest.getName(), memberEntity.getName());
        return RecipeResponse.toResponse(recipeRepository.save(RecipeRequest.toEntity(recipeRequest, memberEntity)));
    }

    @Override
    @Transactional
    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        Recipe recipe = getRecipe(id);
        isAlreadyCreate(recipeRequest.getName(), recipe.getMember().getName());
        return RecipeResponse.toResponse(recipe.update(recipeRequest));
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        getRecipe(id).delete();
    }

    //레시피 이름과 회원 이름으로 레시피 탐색
    @Override
    public Recipe getRecipe(Long id) {

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));

        return isDelete(recipe);
    }

    //레시피가 삭제된 경우 예외처리
    @Override
    public Recipe isDelete(Recipe recipe) {

        if (!recipe.isState()) throw new ApiException(ErrorStatus._DELETE_RECIPE);

        return recipe;
    }

    @Override
    public void isAlreadyCreate(String RecipeName, String memberName) {
        recipeRepository.findAllByNameAndMember_Name(RecipeName, memberName).stream()
                .filter(Recipe::isState)
                .findFirst()
                .ifPresent(recipe -> {
                    throw new ApiException(ErrorStatus._ALREADY_CREATE_RECIPE);
                });
    }

    @Override
    public RecipeResponse.RecipeMyPageList getRecipeMyPageList(Long memberId, Integer page) {
        Page<Recipe> recipePage = recipeRepository.findAllByMemberId(memberId, PageRequest.of(page, 9));


        return toRecipeMyPageList(recipePage);
    }

    @Override
    public RecipeResponse.RecipeMyPageList getRecipeLikeList(Long memberId, Integer page) {
        Page<Recipe> recipePage = recipeRepository.findRecipesByMemberId(memberId, PageRequest.of(page, 9));

        return toRecipeMyPageList(recipePage);
    }

    @Override
    public boolean deleteAllRecipe(Long memberId) {
        recipeRepository.deleteAllByMemberId(memberId);

        return true;
    }

}
