package com.example.dgbackend.domain.termagree.repository;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.term.Term;
import com.example.dgbackend.domain.termagree.TermAgree;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermAgreeRepository extends JpaRepository<TermAgree, Long> {
    List<TermAgree> findAllByMemberId(Long memberId);
}
