package com.example.dgbackend.domain.member.repository;

import com.example.dgbackend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    Boolean existsByProviderAndProviderId(String provider, String providerId);
}
