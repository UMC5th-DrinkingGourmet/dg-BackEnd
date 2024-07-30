package com.example.dgbackend.domain.recipeimage.service;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.repository.RecipeRepository;
import com.example.dgbackend.domain.recipe.service.RecipeService;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import com.example.dgbackend.domain.recipeimage.dto.RecipeImageRequest;
import com.example.dgbackend.domain.recipeimage.dto.RecipeImageResponse;
import com.example.dgbackend.domain.recipeimage.dto.RecipeImageVO;
import com.example.dgbackend.domain.recipeimage.repository.RecipeImageRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import com.example.dgbackend.global.s3.S3Service;
import com.example.dgbackend.global.s3.dto.S3Result;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class RecipeImageService {

    private final RecipeImageRepository recipeImageRepository;
    private final RecipeRepository recipeRepository;

    private final S3Service s3Service;

    public List<String> getRecipeImages(Long recipeId) {
        return recipeImageRepository.findAllByRecipeId(recipeId).stream()
            .map(RecipeImage::getImageUrl)
            .toList();
    }

    @Transactional
    public RecipeImageResponse createRecipeImage(RecipeImageVO.FileVO request) {
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
            .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));

        //파일 없을 시 예외처리
        List<MultipartFile> multipartFiles = validFileList(request.getFileList());

        //S3에 이미지 업로드하고 URL 받아오기
        List<String> imageURLs = s3Service.uploadFile(multipartFiles).stream()
            .map(S3Result::getImgUrl)
            .toList();

        //RecipeImage(url 리스트, recipe) 엔티티 생성
        List<RecipeImage> recipeImages = imageURLs.stream()
            .map(imageURL -> RecipeImageRequest.toEntity(recipe, imageURL))
            .toList();

        //RecipeImage 엔티티 모두 저장
        recipeImageRepository.saveAll(recipeImages);

        return RecipeImageResponse.toURLResponse(recipe);
    }

    //파일 없을 시 예외처리
    private List<MultipartFile> validFileList(List<MultipartFile> request) {

        if (request == null || request.isEmpty()) {
            throw new ApiException(ErrorStatus._NOTHING_RECIPE_IMAGE);
        }

        return request;
    }

    @Transactional
    public RecipeImageResponse updateRecipeImage(RecipeImageVO.FileVO request) {
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
            .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));

        //새로운 이미지 업로드
        if (request.getFileList() != null && !request.getFileList().isEmpty()) {
            createRecipeImage(request);
        }

        List<String> deleteFileUrlList = request.getDeleteFileUrlList();

        //삭제 요청 이미지 삭제
        if (deleteFileUrlList != null && !deleteFileUrlList.isEmpty()) {
            deleteFileUrlList.forEach(
                deleteFileUrl -> {
                    deleteRecipeImage(deleteFileUrl);
                    s3Service.deleteFile(deleteFileUrl);
                }
            );
        }

        return RecipeImageResponse.toURLResponse(recipe);
    }

    //recipeImageRepository에서 imageUrl로 조회해서 있으면 삭제하고 s3에서도 삭제
    //없다면 예외처리
    public void deleteRecipeImage(String imageUrl) {
        recipeImageRepository.findByImageUrl(imageUrl)
            .ifPresentOrElse(recipeImageEntity -> {
                    s3Service.deleteFile(recipeImageEntity.getImageUrl());
                    recipeImageRepository.delete(recipeImageEntity);
                    ;
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


}
