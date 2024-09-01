package com.example.dgbackend.domain.recipeimage.controller;

import com.example.dgbackend.domain.recipeimage.dto.RecipeImageResponse;
import com.example.dgbackend.domain.recipeimage.service.RecipeImageService;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "레시피 이미지 API", description = "레시피 이미지 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe-images")
public class RecipeImageController {

    private final RecipeImageService recipeImageService;


    @Operation(summary = "레시피 이미지 업로드", description = "레시피 이미지 업로드")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<RecipeImageResponse.RecipeImageResult> uploadRecipeImage(
        @RequestPart(name = "imageUrls", required = false) List<MultipartFile> multipartFiles) {
        return ApiResponse.onSuccess(recipeImageService.uploadRecipeImage(multipartFiles));
    }

}
