package com.example.dgbackend.domain.recipe.controller;

import com.example.dgbackend.domain.enums.Gender;
import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recipe.dto.RecipeRequest;
import com.example.dgbackend.domain.recipe.dto.RecipeResponse;
import com.example.dgbackend.domain.recipe.service.RecipeServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "레시피북 API")
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

    @Operation(summary = "모든 레시피북 조회", description = "삭제되지 않은 레시피북 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<RecipeResponse>> getRecipes() {
        return ApiResponse.onSuccess(recipeServiceImpl.getExistRecipes());
    }

    @Operation(summary = "레시피북 상세정보 조회", description = "특정 레시피북 정보를 조회합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/{recipeId}")
    public ApiResponse<RecipeResponse> getRecipe(@PathVariable Long recipeId) {
        return ApiResponse.onSuccess(recipeServiceImpl.getRecipeDetail(recipeId));
    }

    @Operation(summary = "레시피북 등록", description = "레시피북을 등록합니다.")
    @PostMapping
    public ApiResponse<RecipeResponse> createRecipe(@RequestBody RecipeRequest recipeRequest) {
        return ApiResponse.onSuccess(recipeServiceImpl.createRecipe(recipeRequest, member));
    }

    @Operation(summary = "레시피북 수정", description = "레시피북을 수정합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
    @PatchMapping("/{recipeId}")
    public ApiResponse<RecipeResponse> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        return ApiResponse.onSuccess(recipeServiceImpl.updateRecipe(recipeId, recipeRequest));
    }

    @Operation(summary = "레시피북 삭제", description = "레시피북을 삭제합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/{recipeId}")
    public ApiResponse<String> deleteRecipe(@PathVariable Long recipeId) {
        recipeServiceImpl.deleteRecipe(recipeId);
        return ApiResponse.onSuccess("삭제 완료");
    }

}
