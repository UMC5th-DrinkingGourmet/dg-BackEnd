package com.example.dgbackend.domain.memberblock.dto;

import com.example.dgbackend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberBlockResponse {

	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public static class MemberBlockResult {

		Long memberId;
		Long blockedMemberId;
	}

	public static MemberBlockResult toMemberBlockResult(Member blockedMember, Member member) {
		return MemberBlockResult.builder()
			.blockedMemberId(blockedMember.getId())
			.memberId(member.getId())
			.build();
	}

}
