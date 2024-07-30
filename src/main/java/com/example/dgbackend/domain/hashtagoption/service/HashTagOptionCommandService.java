package com.example.dgbackend.domain.hashtagoption.service;

import com.example.dgbackend.domain.combination.Combination;
import java.util.List;

public interface HashTagOptionCommandService {

    void deleteAllHashTagOption(Long combinationId);

    void updateHashTagOption(Combination combination, List<String> hashTagNames);
}
