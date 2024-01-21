package com.example.dgbackend.domain.combinationcomment.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CombinationCommentQueryService {

    Page<CombinationComment> getCombinationCommentFromCombination(Combination combination, PageRequest pageRequest);
}
