package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recommend.Recommend;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendQueryServiceImpl implements RecommendQueryService{
    @Autowired
    RecommendRepository recommendRepository;

    @Override
    public void addRecommend(Member member, RecommendRequest.RecommendRequestDTO recommendRequestDTO, String drinkName, String drinkInfo, String imageUrl) {
        Recommend recommend = Recommend.builder()
                .desireLevel(recommendRequestDTO.getDesireLevel())
                .foodName(recommendRequestDTO.getFoodName())
                .feeling(recommendRequestDTO.getFeeling())
                .weather(recommendRequestDTO.getWeather())
                .drinkName(drinkName)
                .drinkInfo(drinkInfo)
                .imageUrl("temp")
                .member(member)
                .build();
        recommendRepository.save(recommend);
    }
}
