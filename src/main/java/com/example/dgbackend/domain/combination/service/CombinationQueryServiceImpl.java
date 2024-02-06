package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationEditResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationMyPageList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationPreviewResultList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.CombinationResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationDetailResult;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationMyPageList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationPreviewResultList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationResult;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;
import static com.example.dgbackend.domain.member.dto.MemberResponse.toMemberResult;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentQueryService;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.combinationlike.service.CombinationLikeQueryService;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import com.example.dgbackend.domain.hashtagoption.service.HashTagOptionQueryService;
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
    private final CombinationLikeQueryService combinationLikeQueryService;
    private final HashTagOptionQueryService hashTagOptionQueryService;

    /*
    오늘의 조합 홈 조회(페이징)
     */
    @Override
    public CombinationPreviewResultList getCombinationPreviewResultList(Integer page,
        Member loginMember) {
        Page<Combination> combinations = combinationRepository.findAllByState(true,
            PageRequest.of(page, 10));
        List<Combination> combinationList = combinations.getContent();

        List<List<HashTagOption>> hashTagOptionList = combinationList.stream()
            .map(hashTagOptionQueryService::getAllHashTagOptionByCombination)
            .toList();

        List<Boolean> isLikeList = combinationList.stream()
            .map(cb -> combinationLikeQueryService.isCombinationLike(cb, loginMember))
            .toList();

        return toCombinationPreviewResultList(combinations, hashTagOptionList, isLikeList);
    }

    /*
     * 오늘의 조합 상세 조회
     */
    @Override
    public CombinationDetailResult getCombinationDetailResult(Long combinationId,
        Member loginMember) {

        // Combination
        Combination combination = getCombination(combinationId);

        // CombinationLike
        boolean isCombinationLike = combinationLikeQueryService.isCombinationLike(combination,
            loginMember);

        // HashTagOption
        List<HashTagOption> hashTagOptions = hashTagOptionQueryService.getAllHashTagOptionByCombination(
            combination);
        CombinationResult combinationResult = toCombinationResult(combination, hashTagOptions,
            isCombinationLike);

        // Member - 작성자
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
        Combination combination = combinationRepository.findById(combinationId).orElseThrow(
            () -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND)
        );

        return isDelete(combination);
    }

    @Override
    public Combination isDelete(Combination combination) {

        if (!combination.isState()) {
            throw new ApiException(ErrorStatus._DELETE_COMBINATION);
        }
        return combination;
    }

    /*
     * 내가 작성한 오늘의 조합 조회
     */
    @Override
    public CombinationResponse.CombinationMyPageList getCombinationMyPageList(Long memberId, Integer page) {
        Page<Combination> combinations = combinationRepository.findAllByMemberId(memberId, PageRequest.of(page, 9));

        return toCombinationMyPageList(combinations);
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

    @Override
    public CombinationMyPageList getCombinationLikeList(Long memberId, Integer page) {
        Page<Combination> combinations = combinationRepository.findCombinationsByMemberId(memberId, PageRequest.of(page, 9));

        return toCombinationMyPageList(combinations);
    }


    @Override
    public CombinationPreviewResultList findCombinationsListByKeyword(Integer page,
                                                                      String keyword) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Combination> combinations = combinationRepository.findCombinationsByTitleContaining(
                keyword, pageRequest);

        List<Combination> combinationList = combinations.getContent();
        List<List<HashTagOption>> hashTagOptionList = combinationList.stream()
                .map(hashTagOptionRepository::findAllByCombinationWithFetch)
                .toList();

        return toCombinationPreviewResultList(combinations, hashTagOptionList);
    }

    @Override
    public CombinationPreviewResultList findWeeklyBestCombinationsListByKeyWord(Integer page,
                                                                                String keyword) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Combination> combinations = combinationRepository.findCombinationsByTitleContainingAndLikeCountGreaterThanEqualAndStateIsTrueOrderByCreatedAtDesc(
                keyword, pageRequest, 30L);

        List<Combination> combinationList = combinations.getContent();
        List<List<HashTagOption>> hashTagOptionList = combinationList.stream()
                .map(hashTagOptionRepository::findAllByCombinationWithFetch)
                .toList();

        return toCombinationPreviewResultList(combinations, hashTagOptionList);
    }
}

