package com.example.dgbackend.domain.combinationlike.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationlike.CombinationLike;
import com.example.dgbackend.domain.member.Member;
import java.util.Optional;

public interface CombinationLikeQueryService {

    boolean isCombinationLike(Combination combination, Member member);

    Boolean changeCombinationLike(Long memberId, Long combinationId);

    Optional<CombinationLike> getCombinationLikeEntity(Member member, Long combinationId);

    Combination getCombinationEntity(Long combinationId);

    CombinationLike createCombinationLike(Long combinationId, Member member);
}
