package com.example.dgbackend.domain.hashtagoption.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;

import java.util.List;

public interface HashTagOptionQueryService {

    List<HashTagOption> getAllHashTagOptionByCombination(Combination combination);
}
