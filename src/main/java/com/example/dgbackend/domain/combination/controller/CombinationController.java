package com.example.dgbackend.domain.combination.controller;

import com.example.dgbackend.domain.combination.dto.CombinationRequest;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.service.CombinationCommandService;
import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.validation.annotation.CheckPage;
import com.example.dgbackend.global.validation.annotation.ExistCombination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "오늘의 조합 API")
@RestController
@Validated
@RequestMapping("/combinations")
@RequiredArgsConstructor
public class CombinationController {

    private final CombinationQueryService combinationQueryService;
    private final CombinationCommandService combinationCommandService;

    @Operation(summary = "오늘의 조합 홈 조회", description = "오늘의 조합 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 목록 조회 성공")
    })
    @Parameter(name = "page", description = "오늘의 조합 목록 페이지 번호, query string 입니다.")
    @GetMapping("")
    public ApiResponse<CombinationResponse.CombinationPreviewResultList> getCombinations(@CheckPage @RequestParam(name = "page") Integer page) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationPreviewResultList(page));
    }

    @Operation(summary = "오늘의 조합 상세정보 조회", description = "특정 오늘의 조합 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 상세정보 조회 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @GetMapping("/{combinationId}")
    public ApiResponse<CombinationResponse.CombinationDetailResult> getDetailCombination(@ExistCombination @PathVariable(name = "combinationId") Long combinationId) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationDetailResult(combinationId));
    }

    @Operation(summary = "오늘의 조합 작성", description = "오늘의 조합 작성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 작성 성공")
    })
    @Parameter(name = "recommendId", description = "내가 받은 추천 조합 Id, Path Variable 입니다.")
    @PostMapping("/recommends/{recommendId}")
    public ApiResponse<CombinationResponse.CombinationProcResult> writeCombination(@PathVariable(name = "recommendId") Long recommendId,
                                                                                   @RequestPart(name = "writeCombination") CombinationRequest.WriteCombination request) throws IOException {
        // TODO: HttpServletRequest 사용해서 Authorization token으로 사용자 정보 받아오기
        return ApiResponse.onSuccess(combinationCommandService.uploadCombination(recommendId, request));

    }


    @Operation(summary = "오늘의 조합 수정정보 조회", description = "특정 오늘의 조합의 수정할 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 수정 정보 조회 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @GetMapping("/{combinationId}/edit")
    public ApiResponse<CombinationResponse.CombinationEditResult> editCombination(@ExistCombination @PathVariable(name = "combinationId") Long combinationId) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationEditResult(combinationId));
    }

    @Operation(summary = "오늘의 조합 수정", description = "특정 오늘의 조합을 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 수정 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @PatchMapping("/{combinationId}")
    public ApiResponse<CombinationResponse.CombinationProcResult> editProcCombination(@ExistCombination @PathVariable(name = "combinationId") Long combinationId,
                                                                                      @RequestPart(name = "writeCombination") CombinationRequest.WriteCombination request) throws IOException {
        return ApiResponse.onSuccess(combinationCommandService.editCombination(combinationId, request));
    }

    @Operation(summary = "오늘의 조합 삭제", description = "특정 오늘의 조합을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 삭제 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @DeleteMapping("/{combinationId}")
    public ApiResponse<CombinationResponse.CombinationProcResult> deleteCombination(@ExistCombination @PathVariable(name = "combinationId") Long combinationId) {

        return ApiResponse.onSuccess(combinationCommandService.deleteCombination(combinationId));
    }
}
