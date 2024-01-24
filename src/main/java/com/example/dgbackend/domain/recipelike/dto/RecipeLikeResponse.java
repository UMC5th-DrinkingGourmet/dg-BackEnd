package com.example.dgbackend.domain.recipelike.dto;

import com.example.dgbackend.domain.recipelike.RecipeLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeLikeResponse {

    private boolean state;

    public static RecipeLikeResponse toResponseByState(boolean state) {
        return RecipeLikeResponse.builder()
                .state(state)
                .build();
    }

    public static RecipeLikeResponse toResponseByEntity(RecipeLike recipeLike) {
        return RecipeLikeResponse.builder()
                .state(recipeLike.isState())
                .build();
    }

}
