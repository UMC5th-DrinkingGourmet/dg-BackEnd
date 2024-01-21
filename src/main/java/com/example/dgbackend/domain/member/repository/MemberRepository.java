package com.example.dgbackend.domain.member.repository;

import com.example.dgbackend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
