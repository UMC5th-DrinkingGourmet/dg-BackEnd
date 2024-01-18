package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.dto.CombinationResponse;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;

public interface CombinationQueryService {

    CombinationPreviewResultList getCombinationPreviewResultList(Integer page);
    CombinationDetailResult getCombinationDetailResult(Long combinationId);
    CombinationEditResult getCombinationEditResult(Long combinationId);
}
