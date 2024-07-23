package com.example.dgbackend.domain.recipecomment.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentRequest;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentResponse;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentVO;
import com.example.dgbackend.domain.recipecomment.service.RecipeCommentServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
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

@Tag(name = "레시피북 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe-comments")
public class RecipeCommentController {

    private final RecipeCommentServiceImpl recipeCommentService;

    @Operation(summary = "레시피북 댓글 조회", description = "특정 레시피북의 댓글을 조회합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
    @Parameter(name = "page", description = "페이지 번호, Query Param 입니다.", required = true, example = "0", in = ParameterIn.QUERY)
    @GetMapping("/{recipeId}")
    public ApiResponse<RecipeCommentResponse.RecipeCommentResponseList> getRecipeComments(
        @PathVariable Long recipeId, @RequestParam("page") int page,
        @Parameter(hidden = true) @MemberObject Member loginMember) {
        return ApiResponse.onSuccess(
            recipeCommentService.getRecipeComment(recipeId, page, loginMember));
    }

    @Operation(summary = "레시피북 댓글 등록", description = "레시피북 댓글을 등록합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/{recipeId}")
    public ApiResponse<RecipeCommentResponse> saveRecipeComment(@PathVariable Long recipeId,
        @RequestBody RecipeCommentRequest.Post recipeCommentRequest,
        @Parameter(hidden = true) @MemberObject Member loginMember) {
        RecipeCommentVO paramVO = RecipeCommentVO.of(recipeCommentRequest, recipeId,
            loginMember.getName());
        return ApiResponse.onSuccess(recipeCommentService.saveRecipeComment(paramVO, loginMember));
    }

    @Operation(summary = "레시피북 댓글 수정", description = "레시피북 댓글을 수정합니다.")
    @PatchMapping
    public ApiResponse<RecipeCommentResponse> updateRecipeComment(
        @RequestBody RecipeCommentRequest.Patch recipeCommentRequest,
        @Parameter(hidden = true) @MemberObject Member loginMember) {
        return ApiResponse.onSuccess(
            recipeCommentService.updateRecipeComment(recipeCommentRequest, loginMember));
    }

    @Operation(summary = "레시피북 댓글 삭제", description = "레시피북 댓글을 삭제합니다.")
    @Parameter(name = "recipeCommentId", description = "레시피북 댓글 Id, Query Param 입니다.", required = true, example = "1", in = ParameterIn.QUERY)
    @DeleteMapping
    public ApiResponse<RecipeCommentResponse> deleteRecipeComment(
        @RequestParam Long recipeCommentId,
        @Parameter(hidden = true) @MemberObject Member loginMember) {
        return ApiResponse.onSuccess(
            recipeCommentService.deleteRecipeComment(recipeCommentId, loginMember));
    }

}
