package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.dto.CombinationResponse;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;

public interface CombinationQueryService {

    CombinationPreviewDTOList getCombinationPreviewDTOList(Integer page);
    CombinationDetailDTO getCombinationDetailDTO(Long combinationId);
}
