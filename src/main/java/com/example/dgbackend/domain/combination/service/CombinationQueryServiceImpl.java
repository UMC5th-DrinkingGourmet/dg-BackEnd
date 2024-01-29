package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationEditResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationPreviewResultList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationPreviewResultList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationResult;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;
import static com.example.dgbackend.domain.member.dto.MemberResponse.toMemberResult;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentQueryService;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.dto.MemberResponse;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationQueryServiceImpl implements CombinationQueryService {

    private final CombinationRepository combinationRepository;
    private final HashTagOptionRepository hashTagOptionRepository;
    private final CombinationCommentQueryService combinationCommentQueryService;

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

        List<HashTagOption> hashTagOptions = hashTagOptionRepository.findAllByCombinationWithFetch(
            combination);
        CombinationResult combinationResult = toCombinationResult(combination, hashTagOptions);

        // Member
        Member member = combination.getMember();
        MemberResponse.MemberResult memberResult = toMemberResult(member);

        // CombinationComment
        CommentPreViewResult combinationCommentResult = combinationCommentQueryService.getCommentsFromCombination(
            combinationId, 0);

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

        List<HashTagOption> hashTagOptions = hashTagOptionRepository.findAllByCombinationWithFetch(
            combination);

        return CombinationResponse.toCombinationEditResult(combination, hashTagOptions,
            combinationImages);
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

    @Override
    public CombinationPreviewResultList getWeeklyBestCombinationPreviewResultList(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Combination> combinations = combinationRepository.findCombinationsByLikeCountGreaterThanEqualAndStateIsTrueOrderByCreatedAtDesc(
            30L, pageRequest);

        List<Combination> combinationList = combinations.getContent();
        List<List<HashTagOption>> hashTagOptionList = combinationList.stream()
            .map(hashTagOptionRepository::findAllByCombinationWithFetch)
            .toList();

        return toCombinationPreviewResultList(combinations, hashTagOptionList);
    }

}
