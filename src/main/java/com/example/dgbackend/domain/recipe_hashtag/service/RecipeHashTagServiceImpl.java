package com.example.dgbackend.domain.recipe_hashtag.service;

import com.example.dgbackend.domain.hashtag.HashTag;
import com.example.dgbackend.domain.hashtag.repository.HashTagRepository;
import com.example.dgbackend.domain.hashtag.service.HashTagCommandServiceImpl;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe_hashtag.RecipeHashTag;
import com.example.dgbackend.domain.recipe_hashtag.repository.RecipeHashTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeHashTagServiceImpl implements RecipeHashTagService {

    private final RecipeHashTagRepository recipeHashTagRepository;
    private final HashTagCommandServiceImpl hashTagCommandService;
    private final HashTagRepository hashTagRepository;

    @Override
    @Transactional
    public List<RecipeHashTag> uploadRecipeHashTag(Recipe recipe, List<String> hashTagName) {

        List<HashTag> hashTags = hashTagCommandService.getHashTags(hashTagName);

        return hashTags.stream().map(hashTag -> {
                    RecipeHashTag recipeHashTag = RecipeHashTag.builder()
                            .recipe(recipe)
                            .hashtag(hashTag)
                            .build();

                    //있으면 그대로 반환, 없으면 저장 후 반환
                    return recipeHashTagRepository.findByRecipe_IdAndHashtag_Id(recipe.getId(), hashTag.getId())
                            .orElseGet(() -> recipeHashTagRepository.save(recipeHashTag));
                })
                .toList();
    }

    @Override
    public void deleteRecipeHashTag(Long recipeId) {
        recipeHashTagRepository.deleteByRecipe_Id(recipeId);
    }

}
