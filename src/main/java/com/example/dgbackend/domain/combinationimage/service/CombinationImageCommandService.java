package com.example.dgbackend.domain.combinationimage.service;

import com.example.dgbackend.domain.combination.Combination;

import java.util.List;

public interface CombinationImageCommandService {

    void updateCombinationImage(Combination combination, List<String> combinationImageList);
}
