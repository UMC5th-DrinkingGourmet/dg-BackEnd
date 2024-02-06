package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.member.Member;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;

public interface CombinationQueryService {

    CombinationPreviewResultList getCombinationPreviewResultList(Integer page, Member member);

    CombinationDetailResult getCombinationDetailResult(Long combinationId);

    CombinationEditResult getCombinationEditResult(Long combinationId);

    boolean existCombination(Long combinationId);

    Combination getCombination(Long combinationId);

    CombinationMyPageList getCombinationMyPageList(Long memberId, Integer page);

    CombinationPreviewResultList getWeeklyBestCombinationPreviewResultList(Integer page);


    CombinationMyPageList getCombinationLikeList(Long memberId, Integer page);

    CombinationPreviewResultList findCombinationsListByKeyword(Integer page, String keyword);

    CombinationPreviewResultList findWeeklyBestCombinationsListByKeyWord(Integer page,
                                                                         String keyword);


}
