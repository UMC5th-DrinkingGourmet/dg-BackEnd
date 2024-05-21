package com.example.dgbackend.domain.memberblock.repository;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.memberblock.MemberBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBlockRepository extends JpaRepository<MemberBlock, Long> {

	MemberBlock findMemberBlockByMemberAndBlockedMember(Member member, Member blockedMember);
}
