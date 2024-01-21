package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;

public interface CombinationQueryService {

    CombinationPreviewDTOList getCombinationPreviewDTOList(Integer page);
    CombinationDetailDTO getCombinationDetailDTO(Long combinationId);

    CombinationEditDTO getCombinationEditDTO(Long combinationId);
}
