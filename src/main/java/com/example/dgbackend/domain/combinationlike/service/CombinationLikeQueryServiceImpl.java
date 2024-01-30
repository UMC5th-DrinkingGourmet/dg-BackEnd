package com.example.dgbackend.domain.combinationlike.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationlike.repository.CombinationLikeRepository;
import com.example.dgbackend.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CombinationLikeQueryServiceImpl implements CombinationLikeQueryService {

    private final CombinationLikeRepository combinationLikeRepository;

    @Override
    public boolean isCombinationLike(Combination combination, Member member) {

        return combinationLikeRepository.existsByCombinationAndMember(combination, member);
    }
}
