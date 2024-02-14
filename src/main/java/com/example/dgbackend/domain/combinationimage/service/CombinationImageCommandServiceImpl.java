package com.example.dgbackend.domain.combinationimage.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.combinationimage.dto.CombinationImageResponse;
import com.example.dgbackend.domain.combinationimage.repository.CombinationImageRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import com.example.dgbackend.global.s3.dto.S3Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationImageCommandServiceImpl implements CombinationImageCommandService {

    private final CombinationImageRepository combinationImageRepository;
    private final S3Service s3Service;

    @Override
    public CombinationImageResponse.CombinationImageResult uploadImage(
        List<MultipartFile> requestFiles) {

        List<MultipartFile> multipartFiles = validFileList(requestFiles);

        // S3에 이미지 업로드
        List<String> imageUrls = s3Service.uploadFile(multipartFiles)
            .stream()
            .map(S3Result::getImgUrl)
            .toList();

        return CombinationImageResponse.toCombinationImageResult(imageUrls);
    }

    private List<MultipartFile> validFileList(List<MultipartFile> request) {

        if (request == null || request.isEmpty()) {
            throw new ApiException(ErrorStatus._COMBINATION_IMAGE_NOT_FOUND);
        }

        return request;
    }

    @Override
    public void updateCombinationImage(Combination combination, List<String> combinationImageList) {

        // 0. 기존의 이미지 파일 조회
        List<String> existCombinationImageUrls = combinationImageRepository.findAllByCombinationId(
                combination.getId())
            .stream()
            .map(CombinationImage::getImageUrl)
            .toList();

        // 2. 수정하면서 없어진 이미지를 S3에서 삭제하기
        removeCancelledCombinationImage(combinationImageList, existCombinationImageUrls);

        // 3. 새로 추가된 이미지 CombinationImage에 저장하기 (S3에 저장하기)
        addNewCombinationImage(combination, combinationImageList, existCombinationImageUrls);
    }

    private void removeCancelledCombinationImage(List<String> combinationImageList,
        List<String> existCombinationImageUrls) {

        List<String> delImageUrls = existCombinationImageUrls.stream()
            .filter(existImage -> !combinationImageList.contains(existImage))
            .toList();

        delImageUrls.forEach(s3Service::deleteFile);
        delImageUrls.forEach(combinationImageRepository::deleteByImageUrl);
    }

    private void addNewCombinationImage(Combination combination,
        List<String> combinationImageList,
        List<String> existCombinationImageUrls) {

        List<String> addImageUrls = combinationImageList.stream()
            .filter(request -> !existCombinationImageUrls.contains(request))
            .toList();

        List<CombinationImage> combinationImages = addImageUrls.stream()
            .map(image -> CombinationImage.builder()
                .combination(combination)
                .imageUrl(image)
                .build())
            .toList();

        combinationImages.forEach(combination::addCombinationImage);
    }
}
