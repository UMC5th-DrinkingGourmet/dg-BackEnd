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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "레시피북 댓글 API")
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

    @Operation(summary = "레시피북 댓글 조회", description = "특정 레시피북의 댓글을 조회합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/{recipeId}")
    public ApiResponse<List<RecipeCommentResponse>> getRecipeComments(@PathVariable Long recipeId) {
        return ApiResponse.onSuccess(recipeCommentService.getRecipeComment(recipeId));
    }

    @Operation(summary = "레시피북 댓글 등록", description = "레시피북 댓글을 등록합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1", in = ParameterIn.PATH)
    @PostMapping("/{recipeId}")
    public ApiResponse<RecipeCommentResponse> saveRecipeComment(@PathVariable Long recipeId, @RequestBody RecipeCommentRequest.Post recipeCommentRequest) {
        RecipeCommentVO paramVO = RecipeCommentVO.of(recipeCommentRequest, recipeId, defaultmember.getName());
        return ApiResponse.onSuccess(recipeCommentService.saveRecipeComment(paramVO));
    }

    @Operation(summary = "레시피북 댓글 수정", description = "레시피북 댓글을 수정합니다.")
    @PatchMapping
    public ApiResponse<RecipeCommentResponse> updateRecipeComment(@RequestBody RecipeCommentRequest.Patch recipeCommentRequest) {
        return ApiResponse.onSuccess(recipeCommentService.updateRecipeComment(recipeCommentRequest));
    }

    @Operation(summary = "레시피북 댓글 삭제", description = "레시피북 댓글을 삭제합니다.")
    @Parameter(name = "recipeCommentId", description = "레시피북 댓글 Id, Query Param 입니다.", required = true, example = "1", in = ParameterIn.QUERY)
    @DeleteMapping
    public ApiResponse<RecipeCommentResponse> deleteRecipeComment(@RequestParam Long recipeCommentId) {
        return ApiResponse.onSuccess(recipeCommentService.deleteRecipeComment(recipeCommentId));
    }

}
