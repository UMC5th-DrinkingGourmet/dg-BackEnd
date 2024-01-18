package com.example.dgbackend.domain.member.dto;

import com.example.dgbackend.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponse {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MemberResult {
        Long memberId;
        String name;
        String profileImageUrl;
    }

    public static MemberResult toMemberResult(Member member) {
        return MemberResult.builder()
                .memberId(member.getId())
                .name(member.getName())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
