package com.example.dgbackend.domain.recipelike.dto;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeLikeVO {

    private Long RecipeId;
    private String memberName;

    public static RecipeLikeVO of(Long id, String name) {
        return new RecipeLikeVO(id, name);
    }
}
