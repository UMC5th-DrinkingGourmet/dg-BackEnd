package com.example.dgbackend.domain.cachedRecommendImage.repository;

import com.example.dgbackend.domain.cachedRecommendImage.CachedRecommendImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CachedRecommendImageRepository extends JpaRepository<CachedRecommendImage, Long> {

    Optional<List<CachedRecommendImage>> findAllByFoodNameAndDrinkName(String foodName,
        String drinkName);

    Optional<CachedRecommendImage> findByImageUrl(String imageUrl);

    void deleteAllByCreatedAtBefore(LocalDateTime dateTime);

}
