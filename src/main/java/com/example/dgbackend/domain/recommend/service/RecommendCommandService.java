package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;

public interface RecommendCommandService {

    RecommendResponse.RecommendResponseDTO requestRecommend(Member member,
        RecommendRequest.RecommendRequestDTO recommendRequestDTO);

    String makeCombinationImage(Member member, String drinkName,
        RecommendRequest.RecommendRequestDTO requestDTO);

}
