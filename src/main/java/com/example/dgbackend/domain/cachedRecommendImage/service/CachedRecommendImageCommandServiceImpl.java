package com.example.dgbackend.domain.cachedRecommendImage.service;

import com.example.dgbackend.domain.cachedRecommendImage.CachedRecommendImage;
import com.example.dgbackend.domain.cachedRecommendImage.repository.CachedRecommendImageRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CachedRecommendImageCommandServiceImpl implements CachedRecommendImageCommandService {

    private final CachedRecommendImageRepository cachedRecommendImageRepository;

    @Override
    public void saveCachedRecommendImage(String foodName, String drinkName, String imageUrl) {
        if (cachedRecommendImageRepository.findByImageUrl(imageUrl).isPresent()) {
            return;
        }

        cachedRecommendImageRepository.save(CachedRecommendImage.builder()
            .foodName(foodName)
            .drinkName(drinkName)
            .imageUrl(imageUrl)
            .build());
    }

    @Override
    public void deleteAllByTTL(LocalDateTime dateTime) {
        cachedRecommendImageRepository.deleteAllByCreatedAtBefore(dateTime);
    }
}
