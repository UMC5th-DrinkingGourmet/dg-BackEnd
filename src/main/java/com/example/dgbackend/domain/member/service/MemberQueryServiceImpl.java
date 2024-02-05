package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public Boolean existsByProviderAndProviderId(String provider, String providerId) {
        return memberRepository.existsByProviderAndProviderId(provider, providerId);
    }
}
