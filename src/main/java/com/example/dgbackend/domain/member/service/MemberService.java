package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
    }

    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
    }

}
