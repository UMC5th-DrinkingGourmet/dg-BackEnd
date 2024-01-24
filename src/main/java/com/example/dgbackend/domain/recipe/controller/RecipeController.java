package com.example.dgbackend.domain.recipe.controller;

import com.example.dgbackend.domain.enums.Gender;
import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recipe.dto.RecipeRequest;
import com.example.dgbackend.domain.recipe.dto.RecipeResponse;
import com.example.dgbackend.domain.recipe.service.RecipeServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeServiceImpl recipeServiceImpl;

    //Default Member 생성
    //TODO: @AutenticationPrincipal로 변경
    private final MemberRepository memberRepository;
    private Member member = Member.builder()
            .name("김동규").email("email@email.com").birthDate("birthDate")
            .phoneNumber("phoneNumber").nickName("nickName").gender(Gender.MALE).socialType(SocialType.APPLE)
            .build();

    @GetMapping
    public ApiResponse<List<RecipeResponse>> getRecipes() {
        return ApiResponse.onSuccess(recipeServiceImpl.getExistRecipes());
    }

    @GetMapping("/{recipeId}")
    public ApiResponse<RecipeResponse> getRecipe(@PathVariable Long recipeId) {
        return ApiResponse.onSuccess(recipeServiceImpl.getRecipeDetail(recipeId));
    }

    @PostMapping
    public ApiResponse<RecipeResponse> createRecipe(@RequestBody RecipeRequest recipeRequest) {
        return ApiResponse.onSuccess(recipeServiceImpl.createRecipe(recipeRequest, member));
    }

    @PatchMapping("/{recipeId}")
    public ApiResponse<RecipeResponse> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        return ApiResponse.onSuccess(recipeServiceImpl.updateRecipe(recipeId, recipeRequest));
    }

    @DeleteMapping("/{recipeId}")
    public ApiResponse<String> deleteRecipe(@PathVariable Long recipeId) {
        recipeServiceImpl.deleteRecipe(recipeId);
        return ApiResponse.onSuccess("삭제 완료");
    }

}
