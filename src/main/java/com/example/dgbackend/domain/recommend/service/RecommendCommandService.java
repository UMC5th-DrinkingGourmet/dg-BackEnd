package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;

public interface RecommendCommandService {

    RecommendResponse.RecommendResponseDTO requestRecommend(Long memberID, RecommendRequest.RecommendRequestDTO recommendRequestDTO);

    String makeCombinationImage(Long memberID, String drinkName, RecommendRequest.RecommendRequestDTO requestDTO);

}
