package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {
    MemberResponse.RecommendInfoDTO patchRecommendInfo(Long memberID, MemberRequest.RecommendInfoDTO recommendInfoDTO);

    MemberResponse.GetMember patchMember(Long memberID, MemberRequest.PatchMember patchMember);

    MemberResponse.GetMember patchProfileImage(Long memberId, MultipartFile multipartFile);

    void saveMember(Member member);
}
