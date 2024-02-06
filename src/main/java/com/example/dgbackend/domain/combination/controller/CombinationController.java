package com.example.dgbackend.domain.combination.controller;

import com.example.dgbackend.domain.combination.dto.CombinationRequest;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.service.CombinationCommandService;
import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import com.example.dgbackend.global.validation.annotation.CheckCombinationOwner;
import com.example.dgbackend.global.validation.annotation.CheckPage;
import com.example.dgbackend.global.validation.annotation.ExistCombination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping()
    public ApiResponse<CombinationResponse.CombinationPreviewResultList> getCombinations(
        @Parameter(hidden = true) @MemberObject Member loginMember,
        @CheckPage @RequestParam(name = "page") Integer page) {
        return ApiResponse.onSuccess(
            combinationQueryService.getCombinationPreviewResultList(page, loginMember));
    }

    @Operation(summary = "오늘의 조합 상세정보 조회", description = "특정 오늘의 조합 정보를 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 상세정보 조회 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @GetMapping("/{combinationId}")
    public ApiResponse<CombinationResponse.CombinationDetailResult> getDetailCombination(
        @Parameter(hidden = true) @MemberObject Member loginMember,
        @ExistCombination @PathVariable(name = "combinationId") Long combinationId) {
        return ApiResponse.onSuccess(
            combinationQueryService.getCombinationDetailResult(combinationId, loginMember));
    }

    @Operation(summary = "오늘의 조합 작성", description = "오늘의 조합 작성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 작성 성공")
    })
    @Parameter(name = "recommendId", description = "내가 받은 추천 조합 Id, Path Variable 입니다.")
    @PostMapping("/recommends/{recommendId}")
    public ApiResponse<CombinationResponse.CombinationProcResult> writeCombination(
        @Parameter(hidden = true) @MemberObject Member loginMember,
        @PathVariable(name = "recommendId") Long recommendId,
        @RequestBody CombinationRequest.WriteCombination request)
        throws IOException {
        return ApiResponse.onSuccess(
            combinationCommandService.uploadCombination(recommendId, request, loginMember));

    }


    @Operation(summary = "오늘의 조합 수정정보 조회", description = "특정 오늘의 조합의 수정할 정보를 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 수정 정보 조회 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @GetMapping("/{combinationId}/edit")
    public ApiResponse<CombinationResponse.CombinationEditResult> editCombination(
        @Parameter(hidden = true) @MemberObject Member loginMember,
        @CheckCombinationOwner @ExistCombination @PathVariable(name = "combinationId") Long combinationId) {
        return ApiResponse.onSuccess(
            combinationQueryService.getCombinationEditResult(combinationId, loginMember));
    }

    @Operation(summary = "오늘의 조합 수정", description = "특정 오늘의 조합을 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 수정 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @PatchMapping("/{combinationId}")
    public ApiResponse<CombinationResponse.CombinationProcResult> editProcCombination(
        @ExistCombination @PathVariable(name = "combinationId") Long combinationId,
        @RequestBody CombinationRequest.WriteCombination request)
        throws IOException {
        return ApiResponse.onSuccess(
            combinationCommandService.editCombination(combinationId, request));
    }

    @Operation(summary = "오늘의 조합 삭제", description = "특정 오늘의 조합을 삭제합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 삭제 성공")
    })
    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
    @DeleteMapping("/{combinationId}")
    public ApiResponse<CombinationResponse.CombinationProcResult> deleteCombination(
        @ExistCombination @PathVariable(name = "combinationId") Long combinationId) {

        return ApiResponse.onSuccess(combinationCommandService.deleteCombination(combinationId));
    }

    @Operation(summary = "내가 작성한 오늘의 조합 조회", description = "작성한 오늘의 조합 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내가 작성한 오늘의 조합 목록 조회 성공")
    })
    @GetMapping("/my-page")
    public ApiResponse<CombinationResponse.CombinationMyPageList> getMyPageCombinations(@RequestParam(name = "Member Id") Long memberId, @CheckPage @RequestParam(name = "page") Integer page) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationMyPageList(memberId, page));
    }

    @Operation(summary = "주간 베스트 조합 조회", description = "주간 베스트 조합 목록을 조회합니다.")
    @Parameter(name = "page", description = "주간 베스트 조합 목록 페이지 번호, query string 입니다.")
    @GetMapping("/weekly-best")
    public ApiResponse<CombinationResponse.CombinationPreviewResultList> getWeeklyBestCombinations(
        @CheckPage @RequestParam(name = "page") Integer page) {
        return ApiResponse.onSuccess(
            combinationQueryService.getWeeklyBestCombinationPreviewResultList(page));
    }


    @Operation(summary = "내가 좋아요한 오늘의 조합 조회", description = "좋아요를 누른 오늘의 조합 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "내가 좋아요한 오늘의 조합 목록 조회 성공")
    })
    @GetMapping("/likes")
    public ApiResponse<CombinationResponse.CombinationMyPageList> getLikeCombinations(@RequestParam(name = "Member Id") Long memberId, @CheckPage @RequestParam(name = "page") Integer page) {
        return ApiResponse.onSuccess(combinationQueryService.getCombinationLikeList(memberId, page));
    }

    @Operation(summary = "오늘의 조합 검색", description = "오늘의 조합 목록을 검색합니다.")
    @GetMapping("/search")
    public ApiResponse<CombinationResponse.CombinationPreviewResultList> findCombinationsListByKeyWord(
        @RequestParam(name = "page") Integer page, @RequestParam(name = "keyword") String keyword) {
        return ApiResponse.onSuccess(
            combinationQueryService.findCombinationsListByKeyword(page, keyword));
    }

    @Operation(summary = "주간 베스트 조합 검색", description = "주간 베스트 조합 목록을 검색합니다.")
    @GetMapping("/weekly-best/search")
    public ApiResponse<CombinationResponse.CombinationPreviewResultList> findWeeklyBestCombinationsListByKeyWord(
        @RequestParam(name = "page") Integer page, @RequestParam(name = "keyword") String keyword) {
        return ApiResponse.onSuccess(
            combinationQueryService.findWeeklyBestCombinationsListByKeyWord(page, keyword));
    }
}
