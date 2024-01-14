package com.example.dgbackend.domain.member.repository;

import com.example.dgbackend.domain.enums.SocialType;
import com.example.dgbackend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndSocialType(String email, SocialType socialType);
}