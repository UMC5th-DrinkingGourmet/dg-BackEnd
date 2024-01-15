package com.example.dgbackend.domain.combinationlike.controller;

import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "오늘의 조합 좋아요 관련 API")
@RestController
@RequestMapping("/api/combinations/{combinationId}/combinationLikes")
@RequiredArgsConstructor
public class CombinationLikeController {

//    @Operation(summary = "오늘의 조합에 좋아요 추가", description = "특정 오늘의 조합에 좋아요를 추가합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 좋아요 추가 성공")
//    })
//    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
//    @PostMapping("")
//    public ApiResponse<> plusCombinationLike(@PathVariable(name = "combinationId") Long combinationId) {
//
//        return null;
//    }
//
//    @Operation(summary = "오늘의 조합에 좋아요 취소", description = "특정 오늘의 조합에 좋아요를 취소합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 좋아요 취소 성공")
//    })
//    @Parameters({
//            @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다."),
//            @Parameter(name = "combinationLikeId", description = "좋아요 Id, Path Variable 입니다.")
//    })
//    @PatchMapping("/{combinationLikeId}")
//    public ApiResponse<> plusCombinationLike(@PathVariable(name = "combinationId") Long combinationId,
//                                             @PathVariable(name = "combinationLikeId") Long combinationLikeId) {
//
//        return null;
//    }
}
