package com.example.dgbackend.domain.member.repository;

import com.example.dgbackend.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
