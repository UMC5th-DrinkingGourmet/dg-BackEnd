package com.example.dgbackend.domain.member.service;

public interface MemberQueryService {
    Boolean existsByProviderAndProviderId(String provider, String providerId);
}
