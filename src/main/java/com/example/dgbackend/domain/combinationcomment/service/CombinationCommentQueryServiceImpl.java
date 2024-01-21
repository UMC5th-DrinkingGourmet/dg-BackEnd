package com.example.dgbackend.domain.combinationcomment.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.repository.CombinationCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationCommentQueryServiceImpl implements CombinationCommentQueryService{

    private final CombinationCommentRepository combinationCommentRepository;

    @Override
    public Page<CombinationComment> getCombinationCommentFromCombination(Combination combination,
                                                                         PageRequest pageRequest) {

        return combinationCommentRepository.findAllByCombination(combination, pageRequest);
    }
}
