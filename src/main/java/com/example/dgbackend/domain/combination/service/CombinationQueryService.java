package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.Combination;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;

public interface CombinationQueryService {

    CombinationPreviewResultList getCombinationPreviewResultList(Integer page);

    CombinationDetailResult getCombinationDetailResult(Long combinationId);

    CombinationEditResult getCombinationEditResult(Long combinationId);

    Combination getCombination(Long combinationId);
}
