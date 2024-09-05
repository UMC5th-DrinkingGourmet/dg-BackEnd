package com.example.dgbackend.domain.cachedRecommendImage.service;

import java.time.LocalDateTime;

public interface CachedRecommendImageCommandService {

    void saveCachedRecommendImage(String foodName, String drinkName, String imageUrl);

    void deleteAllByTTL(LocalDateTime dateTime);
}
