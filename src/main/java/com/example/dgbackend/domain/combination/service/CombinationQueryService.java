package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationEditResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationMyPageList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationPreviewResultList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationMainList;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.member.Member;

public interface CombinationQueryService {

    CombinationPreviewResultList getCombinationPreviewResultList(Integer page, Member loginMember);

    CombinationDetailResult getCombinationDetailResult(Long combinationId, Member loginMember);

    CombinationEditResult getCombinationEditResult(Long combinationId, Member loginMember);

    boolean existCombination(Long combinationId);

    Combination getCombination(Long combinationId);

    Combination isDelete(Combination combination);

    Boolean isCombinationOwner(Long combinationId, Member member);

    CombinationMyPageList getCombinationMyPageList(Long memberId, Integer page);

    CombinationPreviewResultList getWeeklyBestCombinationPreviewResultList(Member loginMember,
        Integer page);

    CombinationMyPageList getCombinationLikeList(Long memberId, Integer page);

    CombinationPreviewResultList findCombinationsListByKeyword(Member loginMember, Integer page,
        String keyword);

    CombinationPreviewResultList findWeeklyBestCombinationsListByKeyWord(Member loginMember,
        Integer page,
        String keyword);

    CombinationMainList getMainRandomCombinationList();
}
