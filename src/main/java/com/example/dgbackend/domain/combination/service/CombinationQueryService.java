package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationEditResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationPreviewResultList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationMyPageList;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.member.Member;

public interface CombinationQueryService {

    CombinationPreviewResultList getCombinationPreviewResultList(Integer page);

    CombinationDetailResult getCombinationDetailResult(Long combinationId);

    CombinationEditResult getCombinationEditResult(Long combinationId);

    boolean existCombination(Long combinationId);

    Combination getCombination(Long combinationId);

    CombinationMyPageList getCombinationMyPageList(Member member, Integer page);

    CombinationPreviewResultList getWeeklyBestCombinationPreviewResultList(Integer page);

    CombinationMyPageList getCombinationLikeList(Member member, Integer page);

    CombinationPreviewResultList findCombinationsListByKeyword(Integer page, String keyword);

    CombinationPreviewResultList findWeeklyBestCombinationsListByKeyWord(Integer page, String keyword);
}
