package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.recommend.dto.RecommendResponse;

import static com.example.dgbackend.domain.recommend.dto.RecommendResponse.*;

public interface RecommendQueryService {

    RecommendResult getRecommendResult(Long recommendId);
}
