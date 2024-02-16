package com.example.dgbackend.domain.recipelike.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeResponse;
import com.example.dgbackend.domain.recipelike.dto.RecipeLikeVO;
import com.example.dgbackend.domain.recipelike.service.RecipeLikeServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "레시피북 좋아요 API")
@RestController
@RequestMapping("/recipe-likes")
@RequiredArgsConstructor
@Slf4j
public class RecipeLikeController {

    private final RecipeLikeServiceImpl recipeLikeServiceImpl;

    private final MemberRepository memberRepository;

    @Operation(summary = "레시피북 좋아요 조회", description = "특정 레시피북의 좋아요를 조회합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1")
    @GetMapping("/{recipeId}")
    public ApiResponse<RecipeLikeResponse> getRecipeLikes(@PathVariable Long recipeId,
                                                          @MemberObject Member member) {
        return ApiResponse.onSuccess(recipeLikeServiceImpl.getRecipeLike(RecipeLikeVO.of(recipeId, member.getName())));
    }

    @Operation(summary = "레시피북 좋아요 등록", description = "레시피북 좋아요를 등록합니다.")
    @Parameter(name = "recipeId", description = "레시피북 Id, Path Variable 입니다.", required = true, example = "1")
    @PostMapping("/{recipeId}")
    public ApiResponse<RecipeLikeResponse> createRecipeLike(@PathVariable Long recipeId,
                                                            @MemberObject Member member) {
        return ApiResponse.onSuccess(recipeLikeServiceImpl.changeRecipeLike(RecipeLikeVO.of(recipeId, member.getName())));
    }

}
