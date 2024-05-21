package com.example.dgbackend.domain.memberblock.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockRequest.MemberBlockReq;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockResponse;
import com.example.dgbackend.domain.memberblock.service.MemberBlockServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberBlockController {

	private final MemberBlockServiceImpl memberBlockService;

	@PostMapping("/member/blocks")
	public ApiResponse<MemberBlockResponse.MemberBlockResult> block(
		@RequestBody MemberBlockReq memberBlockReq, @MemberObject
	Member member) {
		return ApiResponse.onSuccess(memberBlockService.block(memberBlockReq, member));
	}


}
