package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequest;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {
    @Autowired
    MemberRepository memberRepository;

    @Override
    public MemberResponse.RecommendInfoDTO patchRecommendInfo(Long memberID, MemberRequest.RecommendInfoDTO requestInfoDTO) {
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        member.setPreferredAlcoholType(requestInfoDTO.getPreferredAlcoholType());
        member.setPreferredAlcoholDegree(requestInfoDTO.getPreferredAlcoholDegree());
        member.setDrinkingTimes(requestInfoDTO.getDrinkingTimes());
        member.setDrinkingLimit(requestInfoDTO.getDrinkingLimit());

        memberRepository.save(member);

        return MemberResponse.toRecommendInfoDTO(member);
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
