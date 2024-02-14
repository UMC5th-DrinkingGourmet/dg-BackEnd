package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {
    MemberResponse.RecommendInfoDTO patchRecommendInfo(Member member, MemberRequest.RecommendInfoDTO recommendInfoDTO);

    MemberResponse.GetMember patchMember(Member member, MemberRequest.PatchMember patchMember);

    MemberResponse.GetMember patchProfileImage(Member member, MultipartFile multipartFile);

    MemberResponse.GetMember getMember(Member member);

    void saveMember(Member member);
}
