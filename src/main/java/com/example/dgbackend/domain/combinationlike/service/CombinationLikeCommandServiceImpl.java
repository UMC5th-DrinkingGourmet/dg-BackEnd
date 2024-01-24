package com.example.dgbackend.domain.combinationlike.service;

import com.example.dgbackend.domain.combinationlike.repository.CombinationLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CombinationLikeCommandServiceImpl implements CombinationLikeCommandService {

    private final CombinationLikeRepository combinationLikeRepository;

    @Override
    public void deleteCombinationLike(Long combinationId) {
        combinationLikeRepository.deleteByCombinationId(combinationId);
    }
}
