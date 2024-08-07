package com.example.dgbackend.domain.recipe_hashtag.dto;

import com.example.dgbackend.domain.recipe_hashtag.RecipeHashTag;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecipeHashTagResponse {

    public static List<String> toStringResponse(Set<RecipeHashTag> hashTagNameList) {
        return hashTagNameList.stream()
            .map(hashTag -> hashTag.getHashtag().getName())
            .toList();
    }

}
