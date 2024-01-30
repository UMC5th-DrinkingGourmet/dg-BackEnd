package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentQueryService;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.combinationlike.service.CombinationLikeQueryService;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;
import static com.example.dgbackend.domain.member.dto.MemberResponse.toMemberResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationQueryServiceImpl implements CombinationQueryService {

    private final CombinationRepository combinationRepository;
    private final HashTagOptionRepository hashTagOptionRepository;
    private final CombinationCommentQueryService combinationCommentQueryService;
    private final MemberRepository memberRepository;
    private final CombinationLikeQueryService combinationLikeQueryService;

    /*
    오늘의 조합 홈 조회(페이징)
     */
    @Override
    public CombinationPreviewResultList getCombinationPreviewResultList(Integer page) {
        Page<Combination> combinations = combinationRepository.findAll(PageRequest.of(page, 10));

        List<Combination> combinationList = combinations.getContent();
        List<List<HashTagOption>> hashTagOptionList = combinationList.stream()
                .map(hashTagOptionRepository::findAllByCombinationWithFetch)
                .toList();

        return toCombinationPreviewResultList(combinations, hashTagOptionList);
    }

    /*
     * 오늘의 조합 상세 조회
     */
    @Override
    public CombinationDetailResult getCombinationDetailResult(Long combinationId) {

        // Combination
        Combination combination = combinationRepository.findById(combinationId).orElseThrow(
                () -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND)
        );

        // TODO : Login Member 추후에 Token을 통해 정보 얻기
        Member loginMember = memberRepository.findById(1L).get();

        // CombinationLike
        boolean isCombinationLike = combinationLikeQueryService.isCombinationLike(combination, loginMember);

        // HashTagOption
        List<HashTagOption> hashTagOptions = hashTagOptionRepository.findAllByCombinationWithFetch(combination);
        CombinationResult combinationResult = toCombinationResult(combination, hashTagOptions, isCombinationLike);

        // Member - 작성자
        Member member = combination.getMember();
        MemberResponse.MemberResult memberResult = toMemberResult(member);

        // CombinationComment
        CommentPreViewResult combinationCommentResult = combinationCommentQueryService.getCommentsFromCombination(combinationId, 0);

        return toCombinationDetailResult(combinationResult, memberResult, combinationCommentResult);
    }

    /*
     * 오늘의 조합 수정 정보 조회
     */
    @Override
    public CombinationEditResult getCombinationEditResult(Long combinationId) {

        Combination combination = combinationRepository.findById(combinationId).orElseThrow(
                () -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND)
        );

        List<CombinationImage> combinationImages = combination.getCombinationImages();

        List<HashTagOption> hashTagOptions = hashTagOptionRepository.findAllByCombinationWithFetch(combination);

        return CombinationResponse.toCombinationEditResult(combination, hashTagOptions, combinationImages);
    }

    @Override
    public boolean existCombination(Long combinationId) {
        return combinationRepository.existsById(combinationId);
    }
  
    /*
     * Combination 조회
     */
    @Override
    public Combination getCombination(Long combinationId) {
        return combinationRepository.findById(combinationId).orElseThrow(
                () -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND)
        );
    }
}
