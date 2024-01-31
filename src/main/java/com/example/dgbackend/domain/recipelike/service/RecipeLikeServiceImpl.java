package com.example.dgbackend.domain.recipelike.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.service.MemberService;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.service.RecipeServiceImpl;
import com.example.dgbackend.domain.recipelike.RecipeLike;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeRequest;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeResponse;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeVO;
import com.example.dgbackend.domain.recipelike.repository.RecipeLikeRepository;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeLikeServiceImpl implements RecipeLikeService {

    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeServiceImpl recipeServiceImpl;
    private final MemberService memberService;

    @Override
    public RecipeLikeResponse getRecipeLike(RecipeLikeVO recipeLikeVO) {

        Optional<RecipeLike> recipeLike = getRecipeLikeEntity(recipeLikeVO);

        //레시피 좋아요 엔티티가 존재하면 해당 엔티티의 state를 반환
        //존재하지 않으면 false를 반환
        return recipeLike.map(RecipeLikeResponse::toResponseByEntity)
                .orElseGet(() -> RecipeLikeResponse.toResponseByState(false));
    }

    @Override
    @Transactional
    public RecipeLikeResponse changeRecipeLike(RecipeLikeVO recipeLikeVO) {

        Optional<RecipeLike> recipeLike = getRecipeLikeEntity(recipeLikeVO);

        //있으면 변경, 없으면 만들어서 저장
        RecipeLike savedRecipeLike = recipeLike.map(RecipeLike::changeState)
                .orElseGet(() -> createRecipe(recipeLikeVO));

        return RecipeLikeResponse.toResponseByEntity(savedRecipeLike);
    }

    @Override
    public RecipeLike createRecipe(RecipeLikeVO recipeLikeVO) {
        Recipe recipe = recipeServiceImpl.getRecipe(recipeLikeVO.getRecipeId());
        Member member = memberService.findMemberByName(recipeLikeVO.getMemberName());
        return recipeLikeRepository.save(RecipeLikeRequest.toEntity(recipe, member));
    }


    //레시피 id와 멤버 이름으로 레시피 좋아요 엔티티를 조회
    @Override
    public Optional<RecipeLike> getRecipeLikeEntity(RecipeLikeVO recipeLikeVO) {
        Recipe recipe = recipeServiceImpl.getRecipe(recipeLikeVO.getRecipeId());
        Member memberByName = memberService.findMemberByName(recipeLikeVO.getMemberName());
        return recipeLikeRepository.findByRecipeAndMember(recipe, memberByName);
    }

}
