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
    public void addRecommend(Member member, RecommendRequest.RecommendRequestDTO recommendRequestDTO, String drinkName, String drinkInfo, String imageUrl) {
        Recommend recommend = Recommend.builder()
                .desireLevel(recommendRequestDTO.getDesireLevel())
                .foodName(recommendRequestDTO.getFoodName())
                .feeling(recommendRequestDTO.getFeeling())
                .weather(recommendRequestDTO.getWeather())
                .drinkName(drinkName)
                .drinkInfo(drinkInfo)
                .imageUrl(imageUrl)
                .member(member)
                .build();
        recommendRepository.save(recommend);
    }


    @Override
    public RecommendResponse.RecommendResult getRecommendResult(Long recommendId) {

        Recommend recommend = recommendRepository.findById(recommendId).orElseThrow(
                () -> new ApiException(ErrorStatus._RECOMMEND_NOT_FOUND)
        );

        return RecommendResponse.toRecommendResult(recommend);

    }

    @Override
    public RecommendResponse.RecommendListResult getRecommendListResult(Long memberID, Integer page, Integer size) {
        Member member = memberRepository.findById(memberID).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPTY_MEMBER)
        );

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
    public RecommendResponse.RecommendResult deleteRecommend(Long recommendId) {
        Recommend recommend = recommendRepository.findById(recommendId).orElseThrow(
                () -> new ApiException(ErrorStatus._RECOMMEND_NOT_FOUND)
        );

        s3Service.deleteFile(recommend.getImageUrl());
        recommendRepository.delete(recommend);

        return RecommendResponse.toRecommendResult(recommend);
    }
}