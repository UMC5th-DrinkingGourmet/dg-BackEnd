package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.dgbackend.domain.combination.dto.CombinationResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationQueryServiceImpl implements CombinationQueryService{

    private final CombinationRepository combinationRepository;

    /*
    오늘의 조합 홈 조회(페이징)
     */
    @Override
    public CombinationPreviewDTOList getCombinationPreviewDTOList(Integer page) {
        Page<Combination> combinations = combinationRepository.findAll(PageRequest.of(page, 10));
        return toCombinationPreviewDTOList(combinations);
    }
}
