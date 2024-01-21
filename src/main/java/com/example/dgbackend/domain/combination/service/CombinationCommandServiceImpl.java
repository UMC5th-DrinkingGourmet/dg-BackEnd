package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationRequest;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationimage.domain.CombinationImage;
import com.example.dgbackend.domain.combinationimage.service.CombinationImageQueryService;
import com.example.dgbackend.domain.combinationlike.service.CombinationLikeCommandService;
import com.example.dgbackend.domain.hashtag.service.HashTagCommandService;
import com.example.dgbackend.domain.hashtagoption.service.HashTagOptionCommandService;
import com.example.dgbackend.domain.recommend.domain.Recommend;
import com.example.dgbackend.domain.recommend.repository.RecommendRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import com.example.dgbackend.global.s3.dto.S3Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CombinationCommandServiceImpl implements CombinationCommandService{

    private final CombinationRepository combinationRepository;
    private final RecommendRepository recommendRepository;
    private final S3Service s3Service;
    private final HashTagCommandService hashTagCommandService;
    private final HashTagOptionCommandService hashTagOptionCommandService;
    private final CombinationLikeCommandService combinationLikeCommandService;
    private final CombinationImageQueryService combinationImageQueryService;

    @Override
    public CombinationResponse.CombinationProcResult uploadCombination(Long recommendId, CombinationRequest.WriteCombination request,
                                                 List<MultipartFile> multipartFiles) {

        // TODO : Member 매핑 & JWT 를 통해 파싱

        // Combination & CombinationImage
        Combination newCombination;

        // 업로드 이미지가 없는 경우, GPT가 추천해준 이미지 사용
        if (multipartFiles == null) {
            Recommend recommend = recommendRepository.findById(recommendId).orElseThrow(
                    () -> new ApiException(ErrorStatus._RECOMMEND_NOT_FOUND)
            );
            String recommendImageUrl = recommend.getImageUrl();

            newCombination = createCombination(request.getTitle(), request.getContent(), recommendImageUrl);
        } else {
            List<S3Result> s3Results = s3Service.uploadFile(multipartFiles);

            List<String> imageUrls = s3Results.stream()
                    .map(S3Result::getImgUrl)
                    .toList();

            newCombination = createCombination(request.getTitle(), request.getContent()
                    , imageUrls.toArray(String[]::new));
        }

        Combination saveCombination = combinationRepository.save(newCombination);
        hashTagCommandService.uploadHashTag(saveCombination, request.getHashTagNameList());

        return CombinationResponse.toCombinationProcResult(saveCombination.getId());
    }

    private Combination createCombination(String title, String content, String... imageUrls) {
        Combination combination = Combination.builder()
                .title(title)
                .content(content)
                .combinationImages(new ArrayList<>())
                .build();

        for (String imageUrl : imageUrls) {
            CombinationImage combinationImage = CombinationImage.builder()
                    .imageUrl(imageUrl)
                    .build();
            combination.addCombinationImage(combinationImage);
        }
        return combination;
    }

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


}
