package com.example.dgbackend;

import com.example.dgbackend.domain.combination.service.CombinationScheduler;
import com.example.dgbackend.domain.recipe.service.RecipeScheduler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppInitialization {

    private final CombinationScheduler combinationScheduler;
    private final RecipeScheduler recipeScheduler;

    @Autowired
    public AppInitialization(CombinationScheduler combinationScheduler, RecipeScheduler recipeScheduler) {
        this.combinationScheduler = combinationScheduler;
        this.recipeScheduler = recipeScheduler;
    }

    @PostConstruct
    public void init() {
        // 앱이 초기화될 때 스케줄러를 수동으로 호출하여 업데이트
        combinationScheduler.updateRandomTopLikes();
        recipeScheduler.updateRandomTopLikes();
    }
}