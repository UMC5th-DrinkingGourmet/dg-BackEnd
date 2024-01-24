package com.example.dgbackend.domain.recommend.controller;

import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.service.RecommendCommandService;
import com.example.dgbackend.domain.recommend.service.RecommendQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Recommend", description = "주류 추천 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommends")
public class RecommendController {
    private final RecommendCommandService recommendCommandService;
    private final RecommendQueryService recommendQueryService;

    @Operation(summary = "주류 추천 요청", description = "GPT API에 추류 추천 요청을 보내고 응답을 받아옵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주류 추천 성공"),
    })
    @PostMapping("/request")
    public ApiResponse<RecommendResponse.RecommendResponseDTO> patchRecommendInfo(@RequestParam(name = "MemberID") Long memberID,@RequestBody RecommendRequest.RecommendRequestDTO recommendDTO){
        // TODO : 소셜로그인 통합시 MemberID를 Token에서 추출

        if(recommendDTO.getDesireLevel() == null)
            throw new ApiException(ErrorStatus._NULL_DESIRE_LEVEL);
        if(recommendDTO.getFoodName() == null)
            throw new ApiException(ErrorStatus._NULL_FOOD_NAME);

        RecommendResponse.RecommendResponseDTO response = recommendCommandService.requestRecommend(memberID, recommendDTO);

        return ApiResponse.onSuccess(response);
    }
  


    @Operation(summary = "오늘의 조합 - 추천 받은 조합의 정보 조회", description = "추천 받은 조합을 선택하여 오늘의 조합을 작성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "추천 받은 조합 조회 성공")
    })
    @Parameter(name = "recommendId", description = "내가 받은 추천 조합 Id, Path Variable 입니다.")
    @PostMapping("/{recommendId}")
    public ApiResponse<RecommendResponse.RecommendResult> getRecommend(@PathVariable(name = "recommendId") Long recommendId) {

        return ApiResponse.onSuccess(recommendQueryService.getRecommendResult(recommendId));
    }


