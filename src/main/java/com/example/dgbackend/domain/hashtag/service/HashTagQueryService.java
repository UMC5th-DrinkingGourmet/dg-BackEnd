package com.example.dgbackend.domain.hashtag.service;

import com.example.dgbackend.domain.hashtag.HashTag;
import java.util.Optional;

public interface HashTagQueryService {

    Optional<HashTag> findHashTagByName(String hashTagName);
}
