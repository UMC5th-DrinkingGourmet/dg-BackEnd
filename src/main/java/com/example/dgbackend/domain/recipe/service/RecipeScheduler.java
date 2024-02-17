package com.example.dgbackend.domain.recipe.service;

import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.dto.RecipeResponse;
import com.example.dgbackend.domain.recipe.repository.RecipeRepository;
import com.example.dgbackend.domain.recipeimage.RecipeImage;
import com.example.dgbackend.domain.recipeimage.repository.RecipeImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.dgbackend.domain.recipe.dto.RecipeResponse.toRecipeMainList;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecipeScheduler {
    private final RecipeRepository recipeRepository;
    private final ReentrantLock combinationsLock = new ReentrantLock();
    private final RecipeImageRepository recipeImageRepository;
    private List<Recipe> recipeList = new ArrayList<>();

    @Transactional
    List<Recipe> get3TopRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipeRepository.findAllByStateIsTrueOrderByLikeCountDesc(PageRequest.of(0, 20)).forEach(recipes::add);

        // 리스트가 3개보다 작을 경우, 전체 리스트를 반환
        if (recipes.size() <= 3) {
            return recipes;
        } else {
            Collections.shuffle(recipes);
            return recipes.subList(0, Math.min(recipes.size(), 3));
        }
    }

    private void updateRecipes(List<Recipe> recipes) {
        combinationsLock.lock();
        try {
            recipeList = recipes;
        } finally {
            combinationsLock.unlock();
        }
    }

    @Scheduled(cron = "00 00 00 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void updateRandomTopLikes() {
        // 좋아요가 가장 많은 20개의 항목 가져오기
        List<Recipe> topLikes = get3TopRecipes();

        // 선택된 3개의 항목을 저장하기
        updateRecipes(topLikes);
    }

    public List<Recipe> getMainRecipes() {
        combinationsLock.lock();
        try {
            // 메인 화면에 뿌려줄 3개의 항목 가져오기
            return new ArrayList<>(recipeList);
        } finally {
            combinationsLock.unlock();
        }
    }

    @Transactional
    public RecipeResponse.RecipeMainList getMainTodayRecipeList() {
        List<Recipe> recipes = this.getMainRecipes();

        // 이미지와 해시태그 옵션 리스트 가져오기
        List<RecipeImage> recipeImages = recipes.stream()
                .map(recipe -> recipeImageRepository.findAllByRecipe(recipe)
                        .stream()
                        .findFirst() // 이미지 중 첫 번째 것만 가져옴
                        .orElse(null))  // 만약 이미지가 없다면 null 반환
                .toList();

        return toRecipeMainList(recipes, recipeImages);
    }
}
