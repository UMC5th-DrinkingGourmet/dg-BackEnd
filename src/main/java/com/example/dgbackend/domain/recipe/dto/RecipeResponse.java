package com.example.dgbackend.domain.recipe.dto;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
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
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String info;

    @NotNull
    private String cookingTime;

    @NotNull
    private String calorie;

    @NotNull
    private Long likeCount;

    @NotNull
    private Long commentCount;

    @NotNull
    private String ingredient;

    @NotNull
    private String recipeInstruction;

    private String recommendCombination; //추천받은 조합

    private boolean state = true; //true : 존재, false : 삭제

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
                .name(recipe.getName())
                .recipeImageUrl(imageUrl)
                .build();
    }

}
