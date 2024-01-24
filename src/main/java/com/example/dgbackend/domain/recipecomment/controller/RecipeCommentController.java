package com.example.dgbackend.domain.recipecomment.controller;

import com.example.dgbackend.domain.enums.Gender;
import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentRequest;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentResponse;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentVO;
import com.example.dgbackend.domain.recipecomment.service.RecipeCommentServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe-comments")
public class RecipeCommentController {

    private final RecipeCommentServiceImpl recipeCommentService;

    //Default Member 생성
    //TODO: @AutenticationPrincipal로 변경
    private final MemberRepository memberRepository;
    private Member defaultmember = Member.builder()
            .name("김동규").email("email@email.com").birthDate("birthDate")
            .phoneNumber("phoneNumber").nickName("nickName").gender(Gender.MALE).socialType(SocialType.APPLE)
            .build();

    @GetMapping("/{recipeId}")
    public ApiResponse<List<RecipeCommentResponse>> getRecipeComments(@PathVariable Long recipeId) {
        return ApiResponse.onSuccess(recipeCommentService.getRecipeComment(recipeId));
    }

    @PostMapping("/{recipeId}")
    public ApiResponse<RecipeCommentResponse> saveRecipeComment(@PathVariable Long recipeId, @RequestBody RecipeCommentRequest.Post recipeCommentRequest) {
        RecipeCommentVO paramVO = RecipeCommentVO.of(recipeCommentRequest, recipeId, defaultmember.getName());
        return ApiResponse.onSuccess(recipeCommentService.saveRecipeComment(paramVO));
    }

    @PatchMapping
    public ApiResponse<RecipeCommentResponse> updateRecipeComment(@RequestBody RecipeCommentRequest.Patch recipeCommentRequest) {
        return ApiResponse.onSuccess(recipeCommentService.updateRecipeComment(recipeCommentRequest));
    }

    @DeleteMapping
    public ApiResponse<RecipeCommentResponse> deleteRecipeComment(@RequestParam Long recipeCommentId) {
        return ApiResponse.onSuccess(recipeCommentService.deleteRecipeComment(recipeCommentId));
    }

}
