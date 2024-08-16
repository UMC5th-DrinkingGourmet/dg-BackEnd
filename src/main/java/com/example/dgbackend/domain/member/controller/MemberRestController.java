package com.example.dgbackend.domain.member.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.member.service.MemberCommandService;
import com.example.dgbackend.domain.member.service.MemberQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponse<MemberResponse.RecommendInfoDTO> patchRecommendInfo(
        @MemberObject Member member, @RequestBody MemberRequest.RecommendInfoDTO recommendInfoDTO) {
        return ApiResponse.onSuccess(
            memberCommandService.patchRecommendInfo(member, recommendInfoDTO));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
    })
    @PatchMapping
    public ApiResponse<MemberResponse.GetMember> patchMember(@MemberObject Member member,
        @RequestBody MemberRequest.PatchMember patchMember) {
        return ApiResponse.onSuccess(memberCommandService.patchMember(member, patchMember));
    }

    @Operation(summary = "회원 프로필 이미지 수정", description = "회원 프로필 이미지를 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 프로필 이미지 수정 성공"),
    })
    @PostMapping("/profile-image")
    public ApiResponse<MemberResponse.GetMember> patchProfileImage(@MemberObject Member member,
        @RequestParam(name = "profileImage") MultipartFile patchProfileImage) {

        return ApiResponse.onSuccess(
            memberCommandService.patchProfileImage(member, patchProfileImage));
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
    })
    @GetMapping()
    public ApiResponse<MemberResponse.GetMember> getMember(@MemberObject Member member) {
        return ApiResponse.onSuccess(memberCommandService.getMember(member));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 계정을 삭제합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
    })
    @PostMapping("/cancellations")
    public ApiResponse<Boolean> cancelMember(@MemberObject Member member) {
        return ApiResponse.onSuccess(memberCommandService.deleteMember(member));
    }
}
