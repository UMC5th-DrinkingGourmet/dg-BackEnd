package com.example.dgbackend.domain.combinationlike.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.member.Member;

public interface CombinationLikeQueryService {

    boolean isCombinationLike(Combination combination, Member member);

    Boolean changeCombinationLike(Member member, Long combinationId);
}
