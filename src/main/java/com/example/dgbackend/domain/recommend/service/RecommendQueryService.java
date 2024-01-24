package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import static com.example.dgbackend.domain.recommend.dto.RecommendResponse.RecommendResult;

public interface RecommendQueryService {

    void addRecommend(Member member, RecommendRequest.RecommendRequestDTO recommendRequestDTO, String drinkName, String drinkInfo, String imageUrl);
    RecommendResult getRecommendResult(Long recommendId);
}
