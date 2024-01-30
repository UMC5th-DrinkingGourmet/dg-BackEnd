package com.example.dgbackend.domain.combinationlike.repository;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationlike.CombinationLike;
import com.example.dgbackend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationLikeRepository extends JpaRepository<CombinationLike, Long> {

    void deleteByCombinationId(Long combinationId);

    boolean existsByCombinationAndMember(Combination combination, Member member);
}
