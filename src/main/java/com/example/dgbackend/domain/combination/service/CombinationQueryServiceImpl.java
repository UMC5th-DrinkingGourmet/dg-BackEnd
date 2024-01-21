package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.toCombinationCommentResult;
import static com.example.dgbackend.domain.member.dto.MemberResponse.toMemberResult;
import java.util.List;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationQueryServiceImpl implements CombinationQueryService{

    private final CombinationRepository combinationRepository;
    private final HashTagOptionRepository hashTagOptionRepository;
    private final CombinationCommentQueryService combinationCommentQueryService;

    /*
    오늘의 조합 홈 조회(페이징)
     */
    @Override
    public CombinationPreviewDTOList getCombinationPreviewDTOList(Integer page) {
        Page<Combination> combinations = combinationRepository.findAll(PageRequest.of(page, 10));

        List<Combination> combinationList = combinations.getContent();
        List<List<HashTagOption>> hashTagOptionList = combinationList.stream()
                .map(hashTagOptionRepository::findAllByCombinationWithFetch)
                .toList();

        return toCombinationPreviewDTOList(combinations, hashTagOptionList);
    }

    /*
     * 오늘의 조합 상세 조회
     * @param combinationId
     */
    @Override
    public CombinationDetailDTO getCombinationDetailDTO(Long combinationId) {

        // Combination
        Combination combination = combinationRepository.findById(combinationId).orElseThrow(
                () -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND)
        );

        List<HashTagOption> hashTagOptions = hashTagOptionRepository.findAllByCombinationWithFetch(combination);
        CombinationResult combinationResult = toCombinationResult(combination, hashTagOptions);

        // Member
        Member member = combination.getMember();
        MemberResult memberResult = toMemberResult(member);

        // CombinationComment
        Page<CombinationComment> combinationComments =
                combinationCommentQueryService.getCombinationCommentFromCombination(combination, PageRequest.of(0, 10));

        CombinationCommentResult combinationCommentResult = toCombinationCommentResult(combinationComments);

        return toCombinationDetailDTO(combinationResult, memberResult, combinationCommentResult);
    }

    /*
     * 오늘의 조합 수정 정보 조회
     */
    @Override
    public CombinationEditDTO getCombinationEditDTO(Long combinationId) {

        Combination combination = combinationRepository.findById(combinationId).orElseThrow(
                () -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND)
        );

        List<CombinationImage> combinationImages = combination.getCombinationImages();

        List<HashTagOption> hashTagOptions = hashTagOptionRepository.findAllByCombinationWithFetch(combination);

        return CombinationResponse.toCombinationEditDTO(combination, hashTagOptions, combinationImages);
    }
}
