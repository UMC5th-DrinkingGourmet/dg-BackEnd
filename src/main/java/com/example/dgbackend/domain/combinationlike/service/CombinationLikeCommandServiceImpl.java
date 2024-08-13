package com.example.dgbackend.domain.combinationlike.service;

import com.example.dgbackend.domain.combinationlike.repository.CombinationLikeRepository;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
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

    @Override
    public void deleteAllCombinationLike(Long combinationId) {
        combinationLikeRepository.deleteAllByCombinationId(combinationId);
    }

    @Override
    public void deleteCancellation(Member member) {
        combinationLikeRepository.deleteAllByMember(member);
    }
}
