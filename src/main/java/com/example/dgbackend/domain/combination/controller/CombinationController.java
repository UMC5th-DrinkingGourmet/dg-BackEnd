package com.example.dgbackend.domain.combination.controller;

import com.example.dgbackend.domain.combination.dto.CombinationRequest;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.service.CombinationCommandService;
import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "오늘의 조합 API")
@RestController
@RequestMapping("/api/combinations")
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

    @Operation(summary = "오늘의 조합 작성", description = "오늘의 조합 작성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 작성 성공")
    })
    @Parameter(name = "recommendId", description = "내가 받은 추천 조합 Id, Path Variable 입니다.")
    @PostMapping("/recommends/{recommendId}")
    public ApiResponse<CombinationResponse.CombinationProcResult> writeCombination(@PathVariable(name = "recommendId") Long recommendId,
                                                                                   @RequestPart(name = "writeCombination") CombinationRequest.WriteCombination request,
                                                                                   @RequestPart(name = "imageUrls", required = false) List<MultipartFile> multipartFiles) throws IOException {
        return ApiResponse.onSuccess(combinationCommandService.uploadCombination(recommendId, request, multipartFiles));

    }


    @Operation(summary = "오늘의 조합 수정정보 조회", description = "특정 오늘의 조합의 수정할 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 수정 정보 조회 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @GetMapping("/{combinationId}/edit")
    public ApiResponse<CombinationResponse.CombinationEditDTO> editCombination(@PathVariable(name = "combinationId") Long combinationId) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationEditDTO(combinationId));
    }

}
