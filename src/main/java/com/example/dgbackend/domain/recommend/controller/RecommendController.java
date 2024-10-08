package com.example.dgbackend.domain.recommend.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.service.RecommendCommandService;
import com.example.dgbackend.domain.recommend.service.RecommendQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.config.log.LogExecutionTime;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recommend", description = "주류 추천 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommends")
public class RecommendController {

    private final RecommendCommandService recommendCommandService;
    private final RecommendQueryService recommendQueryService;

    @LogExecutionTime
    @Operation(summary = "주류 추천 요청", description = "GPT API에 추류 추천 요청을 보내고 응답을 받아옵니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주류 추천 성공"),
    })
    @PostMapping("/request")
    public ApiResponse<RecommendResponse.RecommendResponseDTO> patchRecommendInfo(
        @MemberObject Member member,
        @RequestBody RecommendRequest.RecommendRequestDTO recommendDTO) {
        // TODO : 소셜로그인 통합시 MemberID를 Token에서 추출
        return ApiResponse.onSuccess(
            recommendCommandService.requestRecommend(member, recommendDTO));
    }


    @Operation(summary = "오늘의 조합 - 추천 받은 조합의 정보 조회", description = "추천 받은 조합을 선택하여 오늘의 조합을 작성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "추천 받은 조합 조회 성공")
    })
    @Parameter(name = "recommendId", description = "특정 recommend 정보 가져오기, Path Variable 입니다.")
    @PostMapping("/{recommendId}")
    public ApiResponse<RecommendResponse.RecommendResponseDTO> getRecommend(
        @PathVariable(name = "recommendId") Long recommendId) {
        return ApiResponse.onSuccess(recommendQueryService.getRecommendResult(recommendId));
    }

    @Operation(summary = "추천 받은 조합 리스트 조회", description = "유저의 추천 받은 조합을 리스트로 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "추천 받은 조합 리스트 조회 성공")
    })
    @GetMapping("/list")
    public ApiResponse<RecommendResponse.RecommendListResult> getRecommendList(
        @MemberObject Member member, @RequestParam(name = "page") Integer page,
        @RequestParam(name = "size") Integer size) {
        return ApiResponse.onSuccess(
            recommendQueryService.getRecommendListResult(member, page, size));
    }

    @Operation(summary = "오늘의 조합 - 추천 받은 조합 삭제", description = "추천 받은 조합을 선택하여 오늘의 조합을 작성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "추천 받은 조합 삭제 성공")
    })
    @Parameter(name = "recommendId", description = "내가 받은 추천 조합 Id, Path Variable 입니다.")
    @DeleteMapping("/{recommendId}")
    public ApiResponse<RecommendResponse.RecommendResponseDTO> deleteRecommend(
        @PathVariable(name = "recommendId") Long recommendId) {
        return ApiResponse.onSuccess(recommendQueryService.deleteRecommend(recommendId));
    }
}


