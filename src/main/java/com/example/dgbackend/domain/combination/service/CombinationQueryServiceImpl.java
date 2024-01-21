package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationcomment.domain.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentQueryService;
import com.example.dgbackend.domain.member.domain.Member;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.toCombinationCommentResult;
import static com.example.dgbackend.domain.member.dto.MemberResponse.toMemberResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationQueryServiceImpl implements CombinationQueryService{

    private final CombinationRepository combinationRepository;
    private final CombinationCommentQueryService combinationCommentQueryService;

    /*
    오늘의 조합 홈 조회(페이징)
     */
    @Override
    public CombinationPreviewDTOList getCombinationPreviewDTOList(Integer page) {
        Page<Combination> combinations = combinationRepository.findAll(PageRequest.of(page, 10));
        return toCombinationPreviewDTOList(combinations);
    }

    /**
     * 오늘의 조합 상세 조회
     * @param combinationId
     */
    @Override
    public CombinationDetailDTO getCombinationDetailDTO(Long combinationId) {

        // TODO: 예외 처리 - Combination
        Combination combination = combinationRepository.findById(combinationId).get();

        CombinationResult combinationResult = toCombinationResult(combination);

        // Member
        Member member = combination.getMember();
        MemberResponse.MemberResult memberResult = toMemberResult(member);

        // CombinationComment
        Page<CombinationComment> combinationComments =
                combinationCommentQueryService.getCombinationCommentFromCombination(combination, PageRequest.of(0, 10));

        CombinationCommentResponse.CombinationCommentResult combinationCommentResult = toCombinationCommentResult(combinationComments);

        return toCombinationDetailDTO(combinationResult, memberResult, combinationCommentResult);
    }
}
