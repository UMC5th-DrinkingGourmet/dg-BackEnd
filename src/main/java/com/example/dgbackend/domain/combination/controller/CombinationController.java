package com.example.dgbackend.domain.combination.controller;

import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "오늘의 조합 API")
@RestController
@RequestMapping("/combinations")
@RequiredArgsConstructor
public class CombinationController {

    private final CombinationQueryService combinationQueryService;

    @Operation(summary = "오늘의 조합 목록 조회", description = "오늘의 조합 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 목록 조회 성공")
    })
    @Parameter(name = "page", description = "오늘의 조합 목록 페이지 번호, query string 입니다.")
    @GetMapping("")
    public ApiResponse<CombinationResponse.CombinationPreviewDTOList> getCombinations(@RequestParam(name = "page") Integer page) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationPreviewDTOList(page));
    }

    @Operation(summary = "오늘의 조합 상세정보 조회", description = "특정 오늘의 조합 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 상세정보 조회 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @GetMapping("/{combinationId}")
    public ApiResponse<CombinationResponse.CombinationDetailDTO> getDetailCombination(@PathVariable(name = "combinationId") Long combinationId) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationDetailDTO(combinationId));
    }

}
