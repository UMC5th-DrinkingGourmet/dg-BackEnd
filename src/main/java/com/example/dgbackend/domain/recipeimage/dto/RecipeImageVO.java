package com.example.dgbackend.domain.recipeimage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RecipeImageVO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FileVO {
        private Long recipeId;
        private List<MultipartFile> fileList;
        private List<String> deleteFileUrlList;

        public static FileVO of(List<MultipartFile> fileList, Long recipeId, List<String> deleteFileUrlList) {
            return FileVO.builder()
                    .fileList(fileList)
                    .recipeId(recipeId)
                    .deleteFileUrlList(deleteFileUrlList)
                    .build();
        }
    }

}
