package com.example.dgbackend.domain.recommend.service;

import static com.example.dgbackend.domain.recommend.dto.RecommendResponse.RecommendResult;

public interface RecommendQueryService {

    RecommendResult getRecommendResult(Long recommendId);
}
