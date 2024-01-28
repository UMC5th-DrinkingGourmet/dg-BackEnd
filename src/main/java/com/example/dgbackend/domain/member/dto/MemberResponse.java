package com.example.dgbackend.domain.member.dto;

import com.example.dgbackend.domain.enums.Gender;
import com.example.dgbackend.domain.member.Member;
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

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GetMember {
        Long memberId;
        String name;
        String nickName;
        Gender gender;
        String birthDate;
        String phoneNumber;
        String profileImageUrl;
    }

    public static GetMember toGetMember(Member member) {
        return GetMember.builder()
                .memberId(member.getId())
                .name(member.getName())
                .nickName(member.getNickName())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .phoneNumber(member.getPhoneNumber())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RecommendInfoDTO {
        // 주류 추천 정보 입력 DTO
        private String preferredAlcoholType;  //선호 주종
        private String preferredAlcoholDegree; // 선호 도수
        private String drinkingLimit; //주량
        private String drinkingTimes; // 음주 횟수
    }

    public static MemberResponse.RecommendInfoDTO toRecommendInfoDTO(Member member) {
        return MemberResponse.RecommendInfoDTO.builder()
                .preferredAlcoholType(member.getPreferredAlcoholType())
                .preferredAlcoholDegree(member.getPreferredAlcoholDegree())
                .drinkingTimes(member.getDrinkingTimes())
                .drinkingLimit(member.getDrinkingLimit())
                .build();
    }
}
