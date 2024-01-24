package com.example.dgbackend.domain.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeParamVO {

    String name;
    String memberName;

    public static RecipeParamVO of(String name, String memberName) {
        return new RecipeParamVO(name, memberName);
    }
}
