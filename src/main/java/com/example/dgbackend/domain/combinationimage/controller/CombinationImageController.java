package com.example.dgbackend.domain.combinationimage.controller;

import com.example.dgbackend.domain.combinationimage.dto.CombinationImageResponse;
import com.example.dgbackend.domain.combinationimage.service.CombinationImageCommandService;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "오늘의 조합 API")
@RestController
@RequestMapping("/combinationImages")
@RequiredArgsConstructor
public class CombinationImageController {

    private final CombinationImageCommandService combinationImageCommandService;

    @Operation(summary = "오늘의 조합 이미지 업로드", description = "오늘의 조합 이미지를 업로드합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 이미지 업로드 성공")
    })
    @PostMapping("")
    public ApiResponse<CombinationImageResponse.CombinationImageResult> imageUpload(@RequestPart(name = "imageUrls", required = false) List<MultipartFile> multipartFiles) throws IOException {
        return ApiResponse.onSuccess(combinationImageCommandService.uploadImage(multipartFiles));

    }
}
