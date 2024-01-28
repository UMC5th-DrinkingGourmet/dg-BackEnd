package com.example.dgbackend.domain.member.controller;

import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.member.service.MemberCommandService;
import com.example.dgbackend.domain.member.service.MemberQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberRestController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @Operation(summary = "회원 추천 정보 저장", description = "회원 추천 정보를 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 추천 정보 저장 성공"),
    })
    @PatchMapping("/recommend-info")

    public ApiResponse<MemberResponse.RecommendInfoDTO> patchRecommendInfo(@RequestParam(name = "Member ID") Long memberID, @RequestBody MemberRequest.RecommendInfoDTO recommendInfoDTO) {
        // TODO : 소셜로그인 통합시 MemberID를 Token에서 추출

        return ApiResponse.onSuccess(memberCommandService.patchRecommendInfo(memberID, recommendInfoDTO));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
    })
    @PatchMapping
    public ApiResponse<MemberResponse.GetMember> patchMember(@RequestParam(name = "Member ID") Long memberID, @RequestBody MemberRequest.PatchMember patchMember) {
        // TODO : 소셜로그인 통합시 MemberID를 Token에서 추출

        return ApiResponse.onSuccess(memberCommandService.patchMember(memberID, patchMember));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
    })
    @PatchMapping("/sign-out")
    public ApiResponse<String> patchSignOut(@RequestParam(name = "Member ID") Long memberID) {
        // TODO : 소셜로그인 통합시 MemberID를 Token에서 추출

        return ApiResponse.onSuccess(memberCommandService.patchSignOut(memberID));
    }
}
