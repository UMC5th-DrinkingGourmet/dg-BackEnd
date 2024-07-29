package com.example.dgbackend.domain.termagree.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.termagree.dto.TermRequestDTO.TermAgreeRequestDTO;
import com.example.dgbackend.domain.termagree.dto.TermRequestDTO.TermDisagreeRequestDTO;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.MemberTermAgreeResponseDTO;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.TermAgreeResponseDTO;
import com.example.dgbackend.domain.termagree.dto.TermResponseDTO.TermDisagreeResponseDTO;
import com.example.dgbackend.domain.termagree.service.TermAgreeCommandService;
import com.example.dgbackend.domain.termagree.service.TermAgreeQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/term-agree")
public class TermAgreeController {

    private final TermAgreeCommandService termAgreeCommandService;
    private final TermAgreeQueryService termAgreeQueryService;

    @PostMapping("/agree")
    public ApiResponse<TermAgreeResponseDTO> termAgree(@MemberObject Member member,
        @RequestBody TermAgreeRequestDTO termAgreeRequestDTO) {
        return ApiResponse.onSuccess(
            termAgreeCommandService.termAgree(member, termAgreeRequestDTO));
    }

    @PostMapping("/disagree")
    public ApiResponse<TermDisagreeResponseDTO> termDisagree(@MemberObject Member member,
        @RequestBody TermDisagreeRequestDTO termDisagreeRequestDTO) {
        return ApiResponse.onSuccess(
            termAgreeCommandService.termDisagree(member, termDisagreeRequestDTO));
    }

    @GetMapping("/list")
    public ApiResponse<MemberTermAgreeResponseDTO> getMemberTermAgreeList(
        @MemberObject Member member) {
        return ApiResponse.onSuccess(termAgreeQueryService.getMemberTermAgree(member));
    }

}
