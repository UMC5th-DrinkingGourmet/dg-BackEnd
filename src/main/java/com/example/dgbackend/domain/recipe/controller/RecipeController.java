package com.example.dgbackend.domain.recipe.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.dto.RecipeRequest;
import com.example.dgbackend.domain.recipe.dto.RecipeResponse;
import com.example.dgbackend.domain.recipe.service.RecipeScheduler;
import com.example.dgbackend.domain.recipe.service.RecipeServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import com.example.dgbackend.global.validation.annotation.CheckPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "레시피북 API")
@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

	private final RecipeServiceImpl recipeServiceImpl;
	private final RecipeScheduler recipeScheduler;

	@Operation(summary = "모든 레시피북 조회", description = "삭제되지 않은 레시피북 목록을 조회합니다.")
	@Parameter(name = "page", description = "페이지 번호, Query Param 입니다.", required = true, example = "0", in = ParameterIn.QUERY)
	@GetMapping
	public ApiResponse<RecipeResponse.RecipeResponseList> getRecipes(@RequestParam("page") int page,
		@Parameter(hidden = true) @MemberObject Member loginMember) {
		return ApiResponse.onSuccess(recipeServiceImpl.getExistRecipes(page, loginMember));
	}

	@Operation(summary = "레시피북 상세정보 조회", description = "특정 레시피북 정보를 조회합니다.")
	@Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
	@GetMapping("/{recipeId}")
	public ApiResponse<RecipeResponse> getRecipe(@PathVariable Long recipeId,
		@Parameter(hidden = true) @MemberObject Member loginMember) {
		return ApiResponse.onSuccess(recipeServiceImpl.getRecipeDetail(recipeId, loginMember));
	}

	@Operation(summary = "레시피북 등록", description = "레시피북을 등록합니다.")
	@PostMapping
	public ApiResponse<RecipeResponse> createRecipe(@RequestBody RecipeRequest recipeRequest,
		@Parameter(hidden = true) @MemberObject Member loginMember) {
		return ApiResponse.onSuccess(recipeServiceImpl.createRecipe(recipeRequest, loginMember));
	}

	@Operation(summary = "레시피북 수정", description = "레시피북을 수정합니다.")
	@Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
	@PatchMapping("/{recipeId}")
	public ApiResponse<RecipeResponse> updateRecipe(@PathVariable Long recipeId,
		@RequestBody RecipeRequest recipeRequest,
		@Parameter(hidden = true) @MemberObject Member loginMember) {
		return ApiResponse.onSuccess(
			recipeServiceImpl.updateRecipe(recipeId, recipeRequest, loginMember));
	}

	@Operation(summary = "레시피북 삭제", description = "레시피북을 삭제합니다.")
	@Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
	@DeleteMapping("/{recipeId}")
	public ApiResponse<String> deleteRecipe(@PathVariable Long recipeId,
		@Parameter(hidden = true) @MemberObject Member loginMember) {
		return ApiResponse.onSuccess(recipeServiceImpl.deleteRecipe(recipeId, loginMember));
	}

	@Operation(summary = "내가 작성한 레시피북 조회", description = "특정 회원의 레시피북 목록을 조회합니다.")
	@Parameter(name = "page", description = "내가 작성한 레시피북 목록 페이지 번호, query string 입니다.")
	@GetMapping("/my-page")
	public ApiResponse<RecipeResponse.RecipeMyPageList> getMyPageList(
		@Parameter(hidden = true) @MemberObject Member loginMember,
		@CheckPage @RequestParam(name = "page") Integer page) {
		return ApiResponse.onSuccess(recipeServiceImpl.getRecipeMyPageList(loginMember, page));
	}

	@Operation(summary = "내가 좋아요한 레시피북 조회", description = "좋아요를 누른 레시피북 목록을 조회합니다.")
	@Parameter(name = "page", description = "내가 좋아요한 목록 페이지 번호, query string 입니다.")
	@GetMapping("/likes")
	public ApiResponse<RecipeResponse.RecipeMyPageList> getLikeList(
		@Parameter(hidden = true) @MemberObject Member loginMember,
		@CheckPage @RequestParam(name = "page") Integer page) {
		return ApiResponse.onSuccess(recipeServiceImpl.getRecipeLikeList(loginMember, page));
	}

	@Operation(summary = "레시피북 검색", description = "레시피북 목록을 검색합니다.")
	@GetMapping("/search")
	public ApiResponse<RecipeResponse.RecipeResponseList> findRecipesByKeyword(
		@RequestParam(value = "page") Integer page, @RequestParam(value = "keyword") String keyword,
		@Parameter(hidden = true) @MemberObject Member loginMember) {
		return ApiResponse.onSuccess(
			recipeServiceImpl.findRecipesByKeyword(page, keyword, loginMember));
	}

	@Operation(summary = "메인 레시피북 조회", description = "메인에 띄울 레시피북을 조회합니다.")
	@GetMapping("/main")
	public ApiResponse<RecipeResponse.RecipeMainList> getMainRecipeList() {
		return ApiResponse.onSuccess(recipeScheduler.getMainTodayRecipeList());
	}
}

