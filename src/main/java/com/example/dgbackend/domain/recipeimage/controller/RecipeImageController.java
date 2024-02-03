package com.example.dgbackend.domain.recipeimage.controller;

import com.example.dgbackend.domain.recipeimage.dto.RecipeImageResponse;
import com.example.dgbackend.domain.recipeimage.dto.RecipeImageVO;
import com.example.dgbackend.domain.recipeimage.service.RecipeImageService;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "레시피 이미지 API", description = "레시피 이미지 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe-images")
public class RecipeImageController {

    private final S3Service s3Service;
    private final RecipeImageService recipeImageService;

    @Operation(summary = "레시피 이미지 조회", description = "레시피 이미지 조회")
    @Parameter(name = "recipeId", description = "레시피 id", required = true)
    @GetMapping
    public ApiResponse<List<String>> getRecipeImages(@RequestParam("recipeId") Long recipeId) {
        return ApiResponse.onSuccess(recipeImageService.getRecipeImages(recipeId));
    }

    @Operation(summary = "레시피 이미지 생성", description = "레시피 이미지 생성")
    @Parameter(name = "file", description = "파일", required = true)
    @Parameter(name = "recipeId", description = "레시피 id", required = true)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<RecipeImageResponse> createRecipeImage(@RequestPart("file") List<MultipartFile> file, @RequestParam("recipeId") Long recipeId) {

        //파일과 레시피 id로 RecipeImageVO.FileRecipe 생성
        RecipeImageVO.FileVO recipeImageRequestVO = RecipeImageVO.FileVO.of(file, recipeId, null);
        return ApiResponse.onSuccess(recipeImageService.createRecipeImage(recipeImageRequestVO));
    }

    @Operation(summary = "레시피 이미지 수정", description = "레시피 이미지 수정")
    @Parameter(name = "recipeId", description = "레시피 id", required = true)
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<RecipeImageResponse> updateRecipeImage(@RequestPart(value = "file", required = false) @Parameter(name = "file", description = "파일", required = false) List<MultipartFile> file,
                                                              @RequestPart(value = "deleteList", required = false) @Parameter(name = "deleteList", description = "삭제할 파일 url 리스트", required = false) List<String> deleteFileUrlStringList,
                                                              @RequestParam("recipeId") Long recipeId) {

        //파일과 레시피 id, 삭제할 파일 url 리스트로 RecipeImageVO.FileRecipe 생성
        RecipeImageVO.FileVO recipeImageRequestVO = RecipeImageVO.FileVO.of(file, recipeId, deleteFileUrlStringList);
        return ApiResponse.onSuccess(recipeImageService.updateRecipeImage(recipeImageRequestVO));
    }

    @Operation
    @Parameter(name = "recipeId", description = "레시피 id", required = true)
    @DeleteMapping
    public ApiResponse<String> deleteRecipeImage(@RequestParam("recipeId") Long recipeId) {
        recipeImageService.deleteAllRecipeImage(recipeId);
        return ApiResponse.onSuccess("삭제 성공");
    }

}
