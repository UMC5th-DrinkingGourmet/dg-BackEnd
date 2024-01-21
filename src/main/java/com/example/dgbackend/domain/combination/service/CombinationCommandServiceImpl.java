package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationRequest;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationimage.domain.CombinationImage;
import com.example.dgbackend.domain.combinationimage.repository.CombinationImageRepository;
import com.example.dgbackend.domain.hashtag.service.HashTagCommandService;
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
    private final CombinationImageRepository combinationImageRepository;
    private final RecommendRepository recommendRepository;
    private final S3Service s3Service;
    private final HashTagCommandService hashTagCommandService;

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

        return CombinationResponse.toCombinationProcResult(saveCombination);
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

}
