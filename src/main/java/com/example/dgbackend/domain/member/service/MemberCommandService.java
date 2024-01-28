package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;

public interface MemberCommandService {
    MemberResponse.RecommendInfoDTO patchRecommendInfo(Long memberID, MemberRequest.RecommendInfoDTO recommendInfoDTO);
}