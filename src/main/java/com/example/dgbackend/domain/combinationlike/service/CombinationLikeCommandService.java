package com.example.dgbackend.domain.combinationlike.service;

import com.example.dgbackend.domain.member.Member;

public interface CombinationLikeCommandService {

    void deleteCombinationLike(Long combinationId);

    void deleteAllCombinationLike(Long combinationId);

    void deleteCancellation(Member member);
}
