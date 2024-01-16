package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberRequestDTO;
import com.example.dgbackend.domain.member.dto.MemberResponseDTO;
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
public class MemberCommandServiceImpl implements MemberCommandService{
    @Autowired
    MemberRepository memberRepository;
    @Override
    public MemberResponseDTO.RecommendInfoDTO patchRecommendInfo(Long memberID, MemberRequestDTO.RecommendInfoDTO requestInfoDTO) {
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        member.setPreferredAlcoholType(requestInfoDTO.getPreferredAlcoholType());
        member.setPreferredAlcoholDegree(requestInfoDTO.getPreferredAlcoholDegree());
        member.setDrinkingTimes(requestInfoDTO.getDrinkingTimes());
        member.setDrinkingLimit(requestInfoDTO.getDrinkingLimit());

        memberRepository.save(member);

        return MemberResponseDTO.toRecommendInfoDTO(member);
    }
}
