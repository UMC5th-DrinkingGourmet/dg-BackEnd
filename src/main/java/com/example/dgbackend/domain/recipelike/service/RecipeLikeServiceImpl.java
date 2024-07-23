package com.example.dgbackend.domain.recipelike.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.repository.RecipeRepository;
import com.example.dgbackend.domain.recipelike.RecipeLike;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeRequest;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeResponse;
import com.example.dgbackend.domain.recipelike.repository.RecipeLikeRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeLikeServiceImpl implements RecipeLikeService {

    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public RecipeLikeResponse getRecipeLike(Long recipeId, Member member) {

        Optional<RecipeLike> recipeLike = getRecipeLikeEntity(recipeId, member);

        //레시피 좋아요 엔티티가 존재하면 해당 엔티티의 state를 반환
        //존재하지 않으면 false를 반환
        return recipeLike.map(RecipeLikeResponse::toResponseByEntity)
            .orElseGet(() -> RecipeLikeResponse.toResponseByState(false));
    }

    @Override
    @Transactional
    public RecipeLikeResponse changeRecipeLike(Long recipeId, Member member) {

        Optional<RecipeLike> recipeLike = getRecipeLikeEntity(recipeId, member);

        //있으면 변경, 없으면 만들어서 저장
        RecipeLike savedRecipeLike = recipeLike.map(RecipeLike::changeState)
            .orElseGet(() -> createRecipe(recipeId, member));

        return RecipeLikeResponse.toResponseByEntity(savedRecipeLike);
    }

    @Override
    @Transactional
    public RecipeLike createRecipe(Long recipeId, Member member) {
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));

        //레시피 좋아요 엔티티 생성 ㅎ  증가
        RecipeLike save = recipeLikeRepository.save(RecipeLikeRequest.toEntity(recipe, member));
        recipe.changeLikeCount(true);
        return save;
    }


    //레시피 id와 멤버 이름으로 레시피 좋아요 엔티티를 조회
    @Override
    public Optional<RecipeLike> getRecipeLikeEntity(Long recipeId, Member member) {
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));
        return recipeLikeRepository.findByRecipeAndMember(recipe, member);
    }
}
