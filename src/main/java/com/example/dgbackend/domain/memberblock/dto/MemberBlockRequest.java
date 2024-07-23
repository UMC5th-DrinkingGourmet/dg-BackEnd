package com.example.dgbackend.domain.memberblock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberBlockRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberBlockReq {

        Long blockedMemberId; // 차단 대상 member id
    }

}
