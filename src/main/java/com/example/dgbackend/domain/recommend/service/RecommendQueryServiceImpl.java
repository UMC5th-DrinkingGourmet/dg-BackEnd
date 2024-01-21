package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.recommend.Recommend;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.repository.RecommendRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendQueryServiceImpl implements RecommendQueryService{

    private final RecommendRepository recommendRepository;

    @Override
    public RecommendResponse.RecommendResult getRecommendResult(Long recommendId) {

        Recommend recommend = recommendRepository.findById(recommendId).orElseThrow(
                () -> new ApiException(ErrorStatus._RECOMMEND_NOT_FOUND)
        );

        return RecommendResponse.toRecommendResult(recommend);
    }
}
