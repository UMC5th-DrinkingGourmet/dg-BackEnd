package com.example.dgbackend.domain.cachedRecommendImage.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CachedRecommendImageScheduler {
    private final CachedRecommendImageCommandService cachedRecommendImageCommandService;

    @Scheduled(cron = "0 53 16 * * *")
    public void deleteExpiredCachedRecommendImage() {
        cachedRecommendImageCommandService.deleteAllByTTL(LocalDateTime.now().minusDays(7));
    }
}
