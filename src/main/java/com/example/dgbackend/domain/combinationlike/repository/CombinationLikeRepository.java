package com.example.dgbackend.domain.combinationlike.repository;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationlike.CombinationLike;
import com.example.dgbackend.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationLikeRepository extends JpaRepository<CombinationLike, Long> {

    void deleteByCombinationId(Long combinationId);

    boolean existsByCombinationAndMemberAndStateIsTrue(Combination combination, Member member);

    Optional<CombinationLike> findByCombinationAndMember(Combination combination, Member member);

    void deleteAllByCombinationId(Long combinationId);

    void deleteAllByMember(Member member);

}
