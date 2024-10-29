package com.example.dgbackend.domain.combination.service;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationMainList;
import static com.example.dgbackend.domain.combination.dto.CombinationResponse.toCombinationMainPreviewList;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.combinationimage.repository.CombinationImageRepository;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CombinationScheduler {

    private final ReentrantLock combinationsLock = new ReentrantLock();
    private final CombinationRepository combinationRepository;
    private final CombinationImageRepository combinationImageRepository;
    private final HashTagOptionRepository hashTagOptionRepository;

    //메모리에 저장해둘 오늘의 조합 리스트
    private List<Combination> combinations = new ArrayList<>();
    //메모리에 저장해둘 주간 베스트 조합 리스트
    private List<Combination> weeklyCombinations = new ArrayList<>();

    //좋아요수 상위 20개에서 랜덤 3개 가져오기
    @Transactional
    List<Combination> get3TopCombinations() {
        List<Combination> combos = new ArrayList<>();
        combinationRepository.findAllByStateIsTrueOrderByLikeCountDesc(PageRequest.of(0, 20))
            .forEach(combos::add);
        // 리스트가 3개보다 작을 경우, 전체 리스트를 반환
        if (combos.size() <= 3) {
            return combos;
        } else {
            Collections.shuffle(combos);
            return combos.subList(0, Math.min(combos.size(), 3));
        }
    }

    //랜덤 주간 베스트 조합 가져오기
    @Transactional
    List<Combination> getWeeklyCombinations() {
        List<Combination> weeklyCombos = new ArrayList<>();
        combinationRepository.findCombinationsByLikeCountGreaterThanEqualAndStateIsTrue()
            .forEach(weeklyCombos::add);

        return weeklyCombos;
    }

    //오늘의 조합 업데이트
    private void updateCombinations(List<Combination> combos) {
        combinationsLock.lock();
        try {
            combinations = combos;
        } finally {
            combinationsLock.unlock();
        }
    }

    //주간 베스트 조합 업데이트
    private void updateWeeklyCombinations(List<Combination> combos) {
        combinationsLock.lock();
        try {
            weeklyCombinations = combos;
        } finally {
            combinationsLock.unlock();
        }
    }

    //매일 자정에 실행
    @Scheduled(cron = "00 00 00 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void updateRandomTopLikes() {
        // 좋아요가 가장 많은 20개의 항목 가져오기
        List<Combination> topLikes = get3TopCombinations();
        // 선택된 3개의 항목을 저장하기
        updateCombinations(topLikes);

        log.info("실행됨!!!!!!1");

        // 주간 베스트 조합 랜덤 5개 가져오기
        List<Combination> topWeeklyLikes = getWeeklyCombinations();
        updateWeeklyCombinations(topWeeklyLikes);
    }

    //오늘의 조합 리스트 가져오기
    public List<Combination> getMainCombination() {
        combinationsLock.lock();
        try {
            // 메인 화면에 뿌려줄 3개의 항목 가져오기
            return new ArrayList<>(combinations);
        } finally {
            combinationsLock.unlock();
        }
    }

    //주간 베스트 조합 리스트 가져오기
    public List<Combination> getWeeklyCombination() {
        combinationsLock.lock();
        try {
            // 메인 화면에 뿌려줄 5개의 항목 가져오기
            return new ArrayList<>(weeklyCombinations);
        } finally {
            combinationsLock.unlock();
        }
    }

    //오늘의 조합 서비스
    @Transactional
    public CombinationResponse.CombinationMainPreviewList getMainTodayCombinationList() {
        List<Combination> combinations = this.getMainCombination();

        boolean needsUpdate = combinations.stream().anyMatch(combination -> {
            Combination latestCombination = combinationRepository.findById(combination.getId()).orElse(null);
            return latestCombination == null || !latestCombination.isState();
        });

        if (needsUpdate) {
            log.info("소프트 삭제된 조합이 발견되어 리스트를 갱신합니다.");
            updateRandomTopLikes();
            // 리스트 갱신 후 다시 가져오기
            combinations = this.getMainCombination();
        }

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

    //주간 베스트 조합 서비스
    @Transactional
    public CombinationResponse.CombinationMainList getMainRandomCombinationList() {
        List<Combination> combinations = this.getWeeklyCombinations();

        // 이미지와 해시태그 옵션 리스트 가져오기
        List<CombinationImage> combinationImages = combinations.stream()
            .map(combination -> combinationImageRepository.findAllByCombination(combination)
                .stream()
                .findFirst() // 이미지 중 첫 번째 것만 가져옴
                .orElse(null))  // 만약 이미지가 없다면 null 반환
            .toList();

        return toCombinationMainList(combinations, combinationImages);
    }
}
