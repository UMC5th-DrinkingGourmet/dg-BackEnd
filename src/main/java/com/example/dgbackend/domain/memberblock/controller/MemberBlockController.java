package com.example.dgbackend.domain.memberblock.controller;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockRequest.MemberBlockReq;
import com.example.dgbackend.domain.memberblock.dto.MemberBlockResponse;
import com.example.dgbackend.domain.memberblock.service.MemberBlockServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberBlockController {

	private final MemberBlockServiceImpl memberBlockService;

	@Operation(summary = "차단하기", description = "특정 멤버를 차단합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "차단 성공")})
	@PostMapping("/member/blocks")
	public ApiResponse<MemberBlockResponse.MemberBlockResult> block(
		@RequestBody MemberBlockReq memberBlockReq, @MemberObject Member loginMember) {
		return ApiResponse.onSuccess(memberBlockService.block(memberBlockReq, loginMember));
	}


}
