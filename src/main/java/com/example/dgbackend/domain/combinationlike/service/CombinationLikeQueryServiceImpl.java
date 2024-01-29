package com.example.dgbackend.domain.combinationlike.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationlike.CombinationLike;
import com.example.dgbackend.domain.combinationlike.repository.CombinationLikeRepository;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.member.service.MemberService;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CombinationLikeQueryServiceImpl implements CombinationLikeQueryService {

    private final CombinationLikeRepository combinationLikeRepository;

    private final CombinationRepository combinationRepository;

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    @Override
    public boolean isCombinationLike(Combination combination, Member member) {

        return combinationLikeRepository.existsByCombinationAndMember(combination, member);
    }

    @Override
    @Transactional
    public Boolean changeCombinationLike(Long memberId, Long combinationId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_MEMBER));
        Optional<CombinationLike> combinationLike = getCombinationLikeEntity(member, combinationId);

        CombinationLike savedCombinationLike = combinationLike.map(CombinationLike::changeState)
            .orElseGet(() -> createCombinationLike(combinationId, member));

        return savedCombinationLike.nowCombinationLikeState();
    }

    @Override
    public Combination getCombinationEntity(Long combinationId) {
        return combinationRepository.findById(combinationId)
            .orElseThrow(() -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND));
    }

    @Override
    public CombinationLike createCombinationLike(Long combinationId, Member member) {
        return combinationLikeRepository.save(
            new CombinationLike(getCombinationEntity(combinationId), member));
    }

    //조합 id와 멤버 이름으로 조합 좋아요 엔티티를 조회
    @Override
    public Optional<CombinationLike> getCombinationLikeEntity(Member member, Long combinationId) {

        Member memberByName = memberService.findMemberByName(member.getName());

        return combinationLikeRepository.findByCombinationAndMember(
            getCombinationEntity(combinationId), memberByName);
    }
}
