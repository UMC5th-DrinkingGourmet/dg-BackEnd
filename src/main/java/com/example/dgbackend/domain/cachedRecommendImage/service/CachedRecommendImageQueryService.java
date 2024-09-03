package com.example.dgbackend.domain.cachedRecommendImage.service;

import com.example.dgbackend.domain.recommend.Recommend;
import java.util.List;

public interface CachedRecommendImageQueryService {
    List<String> getCachedImageUrl(String foodName, String drinkName);
}
