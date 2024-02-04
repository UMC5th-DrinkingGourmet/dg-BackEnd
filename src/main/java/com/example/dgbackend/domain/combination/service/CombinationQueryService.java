package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationEditResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationPreviewResultList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationMyPageList;

import com.example.dgbackend.domain.combination.Combination;

public interface CombinationQueryService {

    CombinationPreviewResultList getCombinationPreviewResultList(Integer page);

    CombinationDetailResult getCombinationDetailResult(Long combinationId);

    CombinationEditResult getCombinationEditResult(Long combinationId);

    boolean existCombination(Long combinationId);

    Combination getCombination(Long combinationId);

    CombinationMyPageList getCombinationMyPageList(Long memberId, Integer page);

    CombinationPreviewResultList getWeeklyBestCombinationPreviewResultList(Integer page);
}
