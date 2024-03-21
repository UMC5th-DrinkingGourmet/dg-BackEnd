package com.example.dgbackend.domain.recipe.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.dto.RecipeRequest;
import com.example.dgbackend.domain.recipe.dto.RecipeResponse;
import com.example.dgbackend.domain.recipe.repository.RecipeRepository;
import com.example.dgbackend.domain.recipe_hashtag.RecipeHashTag;
import com.example.dgbackend.domain.recipe_hashtag.service.RecipeHashTagService;
import com.example.dgbackend.domain.recipelike.service.RecipeLikeService;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.dgbackend.domain.recipe.dto.RecipeResponse.toRecipeMyPageList;


@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeHashTagService recipeHashTagService;
    private final RecipeLikeService recipeLikeService;

    @Override
    public RecipeResponse.RecipeResponseList getExistRecipes(int page, Member member) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);

        Page<Recipe> allByState = recipeRepository.findAllByStateOrderByCreatedAtDesc(true, pageable);

        List<RecipeResponse> recipeResponseList = allByState.getContent().stream()
                .map(recipe -> {
                    return getRecipeDetailResponse(recipe, member);
                }).toList();

        return RecipeResponse.toRecipeResponseList(allByState, recipeResponseList);
    }

    @Override
    public RecipeResponse getRecipeDetail(Long id, Member member) {
        Recipe recipe = getRecipe(id);
        return getRecipeDetailResponse(recipe, member);
    }

    //like 상태를 추가 (좋아요 누른 상태인지 확인)
    @Override
    public RecipeResponse getRecipeDetailResponse(Recipe recipes, Member member) {

        boolean state = recipeLikeService.getRecipeLike(recipes.getId(), member).isState();

        return RecipeResponse.toResponse(recipes, state);
    }

    @Override
    @Transactional
    public RecipeResponse createRecipe(RecipeRequest recipeRequest, Member memberEntity) {


        //레시피 저장
        Recipe save = recipeRepository.save(RecipeRequest.toEntity(recipeRequest, memberEntity));

        //해시태그 저장
        List<RecipeHashTag> hashTags = recipeHashTagService.uploadRecipeHashTag(save, recipeRequest.getHashTagNameList());

        //레시피에 해시태그 저장
        save.setHashTagList(hashTags);

        return getRecipeDetailResponse(save, memberEntity);
    }

    @Override
    @Transactional
    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        Recipe recipe = getRecipe(id);

        //해시태그 저장
        List<RecipeHashTag> hashTags = recipeHashTagService.uploadRecipeHashTag(recipe, recipeRequest.getHashTagNameList());
        recipe.setHashTagList(hashTags);
        return getRecipeDetailResponse(recipe.update(recipeRequest), recipe.getMember());
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        getRecipe(id).delete();
        recipeHashTagService.deleteRecipeHashTag(id);
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

        if (!recipe.isState()) {
            throw new ApiException(ErrorStatus._DELETE_RECIPE);
        }

        return recipe;
    }

    @Override
    public void isAlreadyCreate(String RecipeName, String memberName) {

        recipeRepository.findAllByTitleAndMember_Name(RecipeName, memberName).stream()
                .filter(Recipe::isState)
                .findFirst()
                .ifPresent(recipe -> {
                    throw new ApiException(ErrorStatus._ALREADY_CREATE_RECIPE);
                });
    }

    @Override
    public RecipeResponse.RecipeResponseList findRecipesByKeyword(Integer page, String keyword, Member member) {

        Pageable pageable = Pageable.ofSize(10).withPage(page);

        Page<Recipe> recipesByKeyword = recipeRepository.findRecipesByTitleContainingAndStateIsTrue(keyword, pageable);

        List<RecipeResponse> recipeResponseList = recipesByKeyword.getContent().stream()
                .map(recipe -> {
                    return getRecipeDetailResponse(recipe, member);
                }).toList();

        return RecipeResponse.toRecipeResponseList(recipesByKeyword, recipeResponseList);
    }

    @Override
    public RecipeResponse.RecipeMyPageList getRecipeMyPageList(Member member,
                                                               Integer page) {
        Page<Recipe> recipePage = recipeRepository.findAllByMemberIdAndStateIsTrueOrderByCreatedAtDesc(member.getId(), PageRequest.of(page, 21));

        return toRecipeMyPageList(recipePage);
    }

    @Override
    public RecipeResponse.RecipeMyPageList getRecipeLikeList(Member member,
                                                             Integer page) {
        Page<Recipe> recipePage = recipeRepository.findRecipesByMemberIdAndStateIsTrue(member.getId(), PageRequest.of(page, 21));

        return toRecipeMyPageList(recipePage);
    }
}
