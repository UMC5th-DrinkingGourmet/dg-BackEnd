package com.example.dgbackend.domain.combinationlike.service;

public interface CombinationLikeCommandService {

    void deleteCombinationLike(Long combinationId);

    boolean deleteAllLike(Long memberId);
}
