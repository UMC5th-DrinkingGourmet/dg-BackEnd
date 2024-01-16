package com.example.dgbackend.domain.recommend.controller;

import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.service.RecommendQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "추천 받은 조합 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecommendController {

    private final RecommendQueryService recommendQueryService;

    @Operation(summary = "오늘의 조합 - 추천 받은 조합의 정보 조회", description = "추천 받은 조합을 선택하여 오늘의 조합을 작성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "추천 받은 조합 조회 성공")
    })
    @Parameter(name = "recommendId", description = "내가 받은 추천 조합 Id, Path Variable 입니다.")
    @GetMapping("/combinations/recommends/{recommendId}")
    public ApiResponse<RecommendResponse.RecommendResult> getRecommend(@PathVariable(name = "recommendId") Long recommendId) {

        return ApiResponse.onSuccess(recommendQueryService.getRecommendResult(recommendId));
    }
}
