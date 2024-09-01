package com.example.dgbackend.domain.combinationlike.controller;


import com.example.dgbackend.domain.combinationlike.service.CombinationLikeQueryServiceImpl;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "오늘의 조합 좋아요 관련 API")
@RestController
@RequiredArgsConstructor
public class CombinationLikeController {

    private final CombinationLikeQueryServiceImpl combinationLikeQueryServiceImpl;


    @Operation(summary = "오늘의 조합 좋아요 누르기", description = "오늘의 조합 좋아요를 등록합니다. 기존에 존재하면 State를 변경합니다.")
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.", required = true)
    @PostMapping("/combination-likes/{combinationId}")
    public ApiResponse<Boolean> updateCombinationLike(@MemberObject Member member,
        @PathVariable(value = "combinationId") Long combinationId) {
        return ApiResponse.onSuccess(
            combinationLikeQueryServiceImpl.changeCombinationLike(member, combinationId));
    }
}