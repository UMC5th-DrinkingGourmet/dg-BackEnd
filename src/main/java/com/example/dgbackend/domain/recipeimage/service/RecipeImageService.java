package com.example.dgbackend.domain.recipeimage.service;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import com.example.dgbackend.domain.recipeimage.dto.RecipeImageResponse;
import com.example.dgbackend.domain.recipeimage.repository.RecipeImageRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import com.example.dgbackend.global.s3.dto.S3Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class RecipeImageService {

    private final RecipeImageRepository recipeImageRepository;

    private final S3Service s3Service;

    public List<String> getRecipeImages(Long recipeId) {
        return recipeImageRepository.findAllByRecipeId(recipeId).stream()
            .map(RecipeImage::getImageUrl)
            .toList();
    }

    //파일 없을 시 예외처리
    private List<MultipartFile> validFileList(List<MultipartFile> request) {

        if (request == null || request.isEmpty()) {
            throw new ApiException(ErrorStatus._NOTHING_RECIPE_IMAGE);
        }

        return request;
    }

    //recipeImageRepository에서 imageUrl로 조회해서 있으면 삭제하고 s3에서도 삭제
    //없다면 예외처리
    public void deleteRecipeImage(String imageUrl) {
        recipeImageRepository.findByImageUrl(imageUrl)
            .ifPresentOrElse(recipeImageEntity -> {
                    s3Service.deleteFile(recipeImageEntity.getImageUrl());
                    recipeImageRepository.delete(recipeImageEntity);
                },
                () -> {
                    throw new ApiException(ErrorStatus._EMPTY_RECIPE_IMAGE);
                });
    }

    public void deleteAllRecipeImage(Long recipeId) {
        recipeImageRepository.findAllByRecipeId(recipeId).forEach(recipeImageEntity -> {
            deleteRecipeImage(recipeImageEntity.getImageUrl());
        });
    }

    public void deleteRecipeImage(Recipe recipe) {
        List<RecipeImage> recipeImages = loadImage(recipe.getId());

        List<String> imageUrls = recipeImages.stream()
            .map(RecipeImage::getImageUrl)
            .toList();

        imageUrls.forEach(s3Service::deleteFile);
        recipeImageRepository.deleteAllByRecipe(recipe);

    }

    private List<RecipeImage> loadImage(Long recipeId) {
        return recipeImageRepository.findAllByRecipeId(recipeId);
    }

    public RecipeImageResponse.RecipeImageResult uploadRecipeImage(
        List<MultipartFile> requestFiles) {

        List<MultipartFile> multipartFiles = validFileList(requestFiles);

        // S3에 이미지 업로드
        List<String> imageUrls = s3Service.uploadFile(multipartFiles)
            .stream()
            .map(S3Result::getImgUrl)
            .toList();

        return RecipeImageResponse.toRecipeImageResult(imageUrls);

    }

    public void updateRecipeImage(Recipe recipe, List<String> recipeImageList) {

        // 1. 기존의 이미지 파일 조회
        List<String> existRecipeImageUrls = recipeImageRepository.findAllByRecipeId(recipe.getId())
            .stream()
            .map(RecipeImage::getImageUrl)
            .toList();

        // 2. 수정하면서 없어진 이미지를 S3에서 삭제하기
        removeCancelledRecipeImage(recipeImageList, existRecipeImageUrls);

        // 3. 새로 추가된 이미지 RecipeImage에 저장하기 (S3에 저장하기)
        addNewRecipeImage(recipe, recipeImageList, existRecipeImageUrls);

    }

    public void removeCancelledRecipeImage(List<String> recipeImageList,
        List<String> existRecipeImageUrls) {

        List<String> delImageUrls = existRecipeImageUrls.stream()
            .filter(existImage -> !recipeImageList.contains(existImage))
            .toList();

        delImageUrls.forEach(recipeImageRepository::deleteByImageUrl);
        delImageUrls.forEach(s3Service::deleteFile);

    }

    public void addNewRecipeImage(Recipe recipe, List<String> recipeImageList,
        List<String> existRecipeImageUrls) {

        List<String> addImageUrls = recipeImageList.stream()
            .filter(request -> !existRecipeImageUrls.contains(request))
            .toList();

        List<RecipeImage> recipeImages = addImageUrls.stream()
            .map(image -> RecipeImage.builder()
                .recipe(recipe)
                .imageUrl(image)
                .build())
            .toList();
        recipeImages.forEach(recipe::addRecipeImage);
    }


}
