package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse.RecommendResponseDTO;

public interface RecommendQueryService {

    void addRecommend(Member member, RecommendRequest.RecommendRequestDTO recommendRequestDTO, String drinkName, String drinkInfo, String imageUrl);

    RecommendResponseDTO getRecommendResult(Long recommendId);

    RecommendResponse.RecommendListResult getRecommendListResult(Member member, Integer page, Integer size);

    RecommendResponseDTO deleteRecommend(Long recommendId);          //추천 삭제
}
