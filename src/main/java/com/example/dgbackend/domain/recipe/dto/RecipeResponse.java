package com.example.dgbackend.domain.recipe.dto;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {

    @NotNull
    @Schema(description = "레시피 Id", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "레시피 이름", example = "김치찌개")
    private String name;

    @NotNull
    @Schema(description = "레시피 소개", example = "맛있는 김치찌개")
    private String info;

    @NotNull
    @Schema(description = "조리시간", example = "30분")
    private String cookingTime;

    @NotNull
    @Schema(description = "칼로리", example = "500kcal")
    private String calorie;

    @NotNull
    @Schema(description = "좋아요 수", example = "10")
    private Long likeCount;

    @NotNull
    @Schema(description = "댓글 수", example = "5")
    private Long commentCount;

    @NotNull
    @Schema(description = "재료", example = "김치, 돼지고기, 두부")
    private String ingredient;

    @NotNull
    @Schema(description = "레시피 순서", example = "1. 김치를 넣는다. 2. 물을 넣는다.")
    private String recipeInstruction;

    @Schema(description = "추천 조합", example = "김치찌개와 참이슬")
    private String recommendCombination; //추천받은 조합

    @Schema(description = "존재 여부", example = "true")
    private boolean state = true; //true : 존재, false : 삭제

    @Schema(description = "작성자 이름", example = "김동규")
    private String memberName;

    public static RecipeResponse toResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .info(recipe.getInfo())
                .cookingTime(recipe.getCookingTime())
                .calorie(recipe.getCalorie())
                .likeCount(recipe.getLikeCount())
                .commentCount(recipe.getCommentCount())
                .ingredient(recipe.getIngredient())
                .recipeInstruction(recipe.getRecipeInstruction())
                .recommendCombination(recipe.getRecommendCombination())
                .state(recipe.isState())
                .memberName(recipe.getMember().getName())
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecipeMyPageList {
        List<RecipeMyPage> recipeList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecipeMyPage {
        private Long id;
        private String name;
        private String recipeImageUrl;
    }

    public static RecipeResponse.RecipeMyPageList toRecipeMyPageList(Page<Recipe> recipes) {

        List<RecipeResponse.RecipeMyPage> recipeMyPages = recipes.getContent()
                .stream()
                .map(rc -> toRecipeMyPage(rc))
                .collect(Collectors.toList());

        return RecipeResponse.RecipeMyPageList.builder()
                .recipeList(recipeMyPages)
                .listSize(recipeMyPages.size())
                .totalPage(recipes.getTotalPages())
                .totalElements(recipes.getTotalElements())
                .isFirst(recipes.isFirst())
                .isLast(recipes.isLast())
                .build();
    }


    public static RecipeMyPage toRecipeMyPage(Recipe recipe) {
        // TODO: 대표 이미지 정하기
        String imageUrl = recipe.getRecipeImageList()
                .stream()
                .findFirst()
                .map(RecipeImage::getImageUrl)
                .orElse(null);

        return RecipeMyPage.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .recipeImageUrl(imageUrl)
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecipeMain {
        private Long id;
        private String recipeName;
        private String cookingTime;
        private String ingredient;
        private String recipeImageUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecipeMainList {
        List<RecipeMain> recipeList;
    }

    public static RecipeMainList toRecipeMainList(List<Recipe> recipes, List<RecipeImage> imgList) {

        List<RecipeMain> recipeMains = recipes
                .stream()
                .map(rc -> toRecipeMain(rc, imgList))
                .collect(Collectors.toList());

        return RecipeResponse.RecipeMainList.builder()
                .recipeList(recipeMains)
                .build();
    }


    public static RecipeMain toRecipeMain(Recipe recipe, List<RecipeImage> imgList) {
        // TODO: 대표 이미지 정하기
        String imageUrl = imgList.stream()
                .filter(img -> img != null && img.getRecipe().getId().equals(recipe.getId()))
                .findAny()  // 이미지 중 첫 번째 것만 가져옴
                .map(img -> img.getImageUrl())
                .orElse(null);  // 만약 이미지가 없다면 null 반환

        return RecipeMain.builder()
                .id(recipe.getId())
                .recipeName(recipe.getName())
                .cookingTime(recipe.getCookingTime())
                .ingredient(recipe.getIngredient())
                .recipeImageUrl(imageUrl)
                .build();
    }

}
