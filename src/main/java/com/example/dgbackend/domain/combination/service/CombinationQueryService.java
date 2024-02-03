package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationEditResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationPreviewResultList;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;

public interface CombinationQueryService {

    CombinationPreviewResultList getCombinationPreviewResultList(Integer page);

    CombinationDetailResult getCombinationDetailResult(Long combinationId);

    CombinationEditResult getCombinationEditResult(Long combinationId);

    boolean existCombination(Long combinationId);

    Combination getCombination(Long combinationId);

    CombinationResponse.CombinationMyPageList getCombinationMyPageList(Long memberId, Integer page);

    CombinationPreviewResultList getWeeklyBestCombinationPreviewResultList(Integer page);
}
