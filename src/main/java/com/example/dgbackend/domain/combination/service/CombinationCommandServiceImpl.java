package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationRequest.createCombination;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationRequest;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationimage.service.CombinationImageCommandService;
import com.example.dgbackend.domain.combinationimage.service.CombinationImageQueryService;
import com.example.dgbackend.domain.combinationlike.service.CombinationLikeCommandService;
import com.example.dgbackend.domain.hashtag.service.HashTagCommandService;
import com.example.dgbackend.domain.hashtagoption.service.HashTagOptionCommandService;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recommend.Recommend;
import com.example.dgbackend.domain.recommend.repository.RecommendRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CombinationCommandServiceImpl implements CombinationCommandService {

    private final CombinationRepository combinationRepository;
    private final RecommendRepository recommendRepository;
    private final S3Service s3Service;
    private final HashTagCommandService hashTagCommandService;
    private final HashTagOptionCommandService hashTagOptionCommandService;
    private final CombinationLikeCommandService combinationLikeCommandService;
    private final CombinationImageQueryService combinationImageQueryService;
    private final CombinationImageCommandService combinationImageCommandService;
    private final MemberRepository memberRepository;

    /**
     * 오늘의 조합 작성
     */
    @Override
    public CombinationResponse.CombinationProcResult uploadCombination(Long recommendId,
        CombinationRequest.WriteCombination request, Member loginMember) {

        // Combination & CombinationImage
        Combination newCombination;
        List<String> combinationImageList = request.getCombinationImageList();

        // 업로드 이미지가 없는 경우, GPT가 추천해준 이미지 사용
        if (combinationImageList == null || combinationImageList.isEmpty()) {
            Recommend recommend = recommendRepository.findById(recommendId).orElseThrow(
                () -> new ApiException(ErrorStatus._RECOMMEND_NOT_FOUND)
            );
            String recommendImageUrl = recommend.getImageUrl();

            newCombination = createCombination(loginMember, request.getTitle(),
                request.getContent(),
                recommendImageUrl);
        } else {
            newCombination = createCombination(loginMember, request.getTitle(), request.getContent()
                , combinationImageList.toArray(String[]::new));
        }
        Combination saveCombination = combinationRepository.save(newCombination);
        hashTagCommandService.uploadHashTag(saveCombination, request.getHashTagNameList());

        return CombinationResponse.toCombinationProcResult(saveCombination.getId());
    }


    /**
     * 오늘의 조합 삭제
     */
    @Override
    public CombinationResponse.CombinationProcResult deleteCombination(Long combinationId) {

        // HashTagOption 삭제
        hashTagOptionCommandService.deleteHashTagOption(combinationId);

        // CombinationLike 삭제
        combinationLikeCommandService.deleteCombinationLike(combinationId);

        // S3에 해당 Combination 이미지 삭제
        List<String> imageUrls = combinationImageQueryService.getCombinationImageUrl(combinationId);
        deleteS3Image(imageUrls);

        // combination 삭제 - 양방향 매핑 CombinationImage, CombinationComment 함께 삭제
        combinationRepository.deleteById(combinationId);
        return CombinationResponse.toCombinationProcResult(combinationId);
    }

    // S3와 같이 저장된 CombinationImage 삭제
    private void deleteS3Image(List<String> imageUrls) {
        imageUrls.forEach(s3Service::deleteFile);
    }

    /**
     * 오늘의 조합 수정
     */
    @Override
    public CombinationResponse.CombinationProcResult editCombination(Long combinationId, CombinationRequest.WriteCombination request) {

        Combination combination = combinationRepository.findById(combinationId).orElseThrow(
                () -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND)
        );

        // Combination - title, content 수정
        combination.updateCombination(request.getTitle(), request.getContent());

        // HashTagOption, HashTag - name 수정
        hashTagOptionCommandService.updateHashTagOption(combination, request.getHashTagNameList());

        // CombinationImage - imageUrl 수정
        combinationImageCommandService.updateCombinationImage(combination, request.getCombinationImageList());

        return CombinationResponse.toCombinationProcResult(combinationId);
    }

    @Override
    public boolean deleteAllCombination(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPTY_MEMBER)
        );
        List<Combination> combinationList = combinationRepository.findAllByMember(member);
        for (Combination combination : combinationList) {
            deleteCombination(combination.getId());
        }
        return true;
    }
}
