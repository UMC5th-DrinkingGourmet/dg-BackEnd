package com.example.dgbackend.domain.cachedRecommendImage.service;

import com.example.dgbackend.domain.cachedRecommendImage.CachedRecommendImage;
import com.example.dgbackend.domain.cachedRecommendImage.repository.CachedRecommendImageRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CachedRecommendImageQueryServiceImpl implements CachedRecommendImageQueryService{
    private final CachedRecommendImageRepository cachedRecommendImageRepository;

    @Override
    public List<String> getCachedImageUrl(String foodName, String drinkName) {
        return cachedRecommendImageRepository.findAllByFoodNameAndDrinkName(foodName, drinkName).orElse(new ArrayList<>())
            .stream().map(CachedRecommendImage::getImageUrl).toList();
    }
}
