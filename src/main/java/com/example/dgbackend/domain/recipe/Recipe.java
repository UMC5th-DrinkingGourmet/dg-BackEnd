package com.example.dgbackend.domain.recipe;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.dto.RecipeRequestDTO;
import com.example.dgbackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Builder.Default
    private boolean state = true; //true : 존재, false : 삭제

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Recipe update(RecipeRequestDTO recipeResponseDto) {
        this.name = recipeResponseDto.getName();
        this.info = recipeResponseDto.getInfo();
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

}
