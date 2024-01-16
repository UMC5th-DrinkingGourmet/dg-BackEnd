package com.example.dgbackend.domain.hashtag.service;

import com.example.dgbackend.domain.combination.domain.Combination;

import java.util.List;

public interface HashTagCommandService {

    void uploadHashTag(Combination combination, List<String> hashTageNames);
}
