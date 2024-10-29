package com.example.dgbackend.domain.recipe;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.dto.RecipeRequest;
import com.example.dgbackend.domain.recipe_hashtag.RecipeHashTag;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

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

    @Builder.Default
    private boolean state = true; //true : 존재, false : 삭제

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeImage> recipeImageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<RecipeHashTag> recipeHashTagList = new HashSet<>();

    public Recipe update(RecipeRequest recipeResponseDto) {
        this.title = recipeResponseDto.getTitle();
        this.cookingTime = recipeResponseDto.getCookingTime();
        this.calorie = recipeResponseDto.getCalorie();
        this.ingredient = recipeResponseDto.getIngredient();
        this.recipeInstruction = recipeResponseDto.getRecipeInstruction();
        this.recommendCombination = recipeResponseDto.getRecommendCombination();
        return this;
    }

    public Recipe delete() {
        this.state = false;
        return this;
    }

    public boolean isState() {
        return this.state;
    }

    public void changeLikeCount(boolean isIncrease) {

        if (isIncrease) {
            this.likeCount++;
        } else {
            this.likeCount--;
        }

    }

    public void changeCommentCount(boolean isIncrease) {

        if (isIncrease) {
            this.commentCount++;
        } else {
            this.commentCount--;
        }

    }

    public void setHashTagList(List<RecipeHashTag> recipeHashTagList) {
        this.recipeHashTagList = new HashSet<>(recipeHashTagList);
    }

    public void addRecipeImage(RecipeImage recipeImage) {
        recipeImageList.add(recipeImage);
        recipeImage.setRecipe(this);
    }

}
