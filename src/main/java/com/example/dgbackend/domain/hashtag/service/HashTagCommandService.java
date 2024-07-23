package com.example.dgbackend.domain.hashtag.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.hashtag.HashTag;
import java.util.List;

public interface HashTagCommandService {

    void uploadHashTag(Combination combination, List<String> hashTageNames);

    List<HashTag> getHashTags(List<String> hashTageNames);
}
