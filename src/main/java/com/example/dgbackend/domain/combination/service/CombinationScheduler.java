package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.combinationimage.repository.CombinationImageRepository;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
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

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationMainPreviewList;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CombinationScheduler {
    private final ReentrantLock combinationsLock = new ReentrantLock();
    private final CombinationRepository combinationRepository;
    private final CombinationImageRepository combinationImageRepository;
    private final HashTagOptionRepository hashTagOptionRepository;

    private List<Combination> combinations = new ArrayList<>();

    @Transactional
    List<Combination> get3TopCombinations() {
        List<Combination> combos = new ArrayList<>();
        combinationRepository.findAllByOrderByLikeCountDesc(PageRequest.of(0, 20)).forEach(combos::add);

        // 리스트가 3개보다 작을 경우, 전체 리스트를 반환
        if (combos.size() <= 3) {
            return combos;
        } else {
            Collections.shuffle(combos);
            return combos.subList(0, Math.min(combos.size(), 3));
        }
    }

    private void updateCombinations(List<Combination> combos) {
        combinationsLock.lock();
        try {
            combinations = combos;
        } finally {
            combinationsLock.unlock();
        }
    }

    @Scheduled(cron = "00 00 00 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void updateRandomTopLikes() {
        // 좋아요가 가장 많은 20개의 항목 가져오기
        List<Combination> topLikes = get3TopCombinations();

        // 선택된 3개의 항목을 저장하기
        updateCombinations(topLikes);
    }

    public List<Combination> getMainCombination() {
        combinationsLock.lock();
        try {
            // 메인 화면에 뿌려줄 3개의 항목 가져오기
            return new ArrayList<>(combinations);
        } finally {
            combinationsLock.unlock();
        }
    }

    @Transactional
    public CombinationResponse.CombinationMainPreviewList getMainTodayCombinationList() {
        List<Combination> combinations = this.getMainCombination();

        // 이미지와 해시태그 옵션 리스트 가져오기
        List<CombinationImage> combinationImages = combinations.stream()
                .map(combination -> combinationImageRepository.findAllByCombination(combination)
                        .stream()
                        .findFirst() // 이미지 중 첫 번째 것만 가져옴
                        .orElse(null))  // 만약 이미지가 없다면 null 반환
                .toList();


        List<List<HashTagOption>> hashTagOptionList = combinations.stream()
                .map(hashTagOptionRepository::findAllByCombinationWithFetch)
                .toList();

        return toCombinationMainPreviewList(combinations, combinationImages, hashTagOptionList);
    }
}
