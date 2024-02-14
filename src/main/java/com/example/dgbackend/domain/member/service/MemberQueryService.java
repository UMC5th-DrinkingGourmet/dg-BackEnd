package com.example.dgbackend.domain.member.service;

import com.example.dgbackend.domain.member.Member;
import java.util.Optional;

public interface MemberQueryService {
    Boolean existsByProviderAndProviderId(String provider, String providerId);

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
}
