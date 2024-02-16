package com.example.dgbackend.domain.recommend.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recommend.Recommend;
import com.example.dgbackend.domain.recommend.dto.RecommendRequest;
import com.example.dgbackend.domain.recommend.dto.RecommendResponse;
import com.example.dgbackend.domain.recommend.repository.RecommendRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendQueryServiceImpl implements RecommendQueryService {

    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Override
    public Recommend addRecommend(Member member, RecommendRequest.RecommendRequestDTO recommendRequestDTO, String drinkName, String drinkInfo, String imageUrl) {
        Recommend recommend = Recommend.builder()
                .desireLevel(recommendRequestDTO.getDesireLevel())
                .foodName(recommendRequestDTO.getFoodName())
                .feeling(recommendRequestDTO.getFeeling())
                .weather(recommendRequestDTO.getWeather())
                .drinkName(drinkName)
                .drinkInfo(drinkInfo)
                .imageUrl(imageUrl)
                .member(member)
//                .deleted(false)
                .build();
        recommendRepository.save(recommend);

        return recommend;
    }
        
    @Override
    public RecommendResponse.RecommendResponseDTO getRecommendResult(Long recommendId) {

        return RecommendResponse.toRecommendResult(getRecommend(recommendId));

    }

    @Override
    public Recommend getRecommend(Long recommendId) {
        return recommendRepository.findById(recommendId).orElseThrow(
            () -> new ApiException(ErrorStatus._RECOMMEND_NOT_FOUND)
        );
    }

    @Override
    public RecommendResponse.RecommendListResult getRecommendListResult(Member member, Integer page, Integer size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Recommend> pageList = recommendRepository.findAllByMemberId(member.getId(), pageable);

        return RecommendResponse.RecommendListResult.builder()
                .recommendResponseDTOList(pageList.map(RecommendResponse::toRecommendResult).toList())
                .listSize(pageList.getSize())
                .totalPage(pageList.getTotalPages())
                .totalElements(pageList.getTotalElements())
                .isFirst(pageList.isFirst())
                .isLast(pageList.isLast())
                .build();
    }

    @Override
    public RecommendResponse.RecommendResponseDTO deleteRecommend(Long recommendId) {
        Recommend recommend = recommendRepository.findById(recommendId).orElseThrow(
                () -> new ApiException(ErrorStatus._RECOMMEND_NOT_FOUND)
        );

        s3Service.deleteFile(recommend.getImageUrl());
        recommendRepository.delete(recommend);

        return RecommendResponse.toRecommendResult(recommend);
    }
}