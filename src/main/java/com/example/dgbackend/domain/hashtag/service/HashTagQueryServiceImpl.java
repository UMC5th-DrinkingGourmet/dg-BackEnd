package com.example.dgbackend.domain.hashtag.service;

import com.example.dgbackend.domain.hashtag.HashTag;
import com.example.dgbackend.domain.hashtag.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashTagQueryServiceImpl implements HashTagQueryService{

    private final HashTagRepository hashTagRepository;

    @Override
    public Optional<HashTag> findHashTagByName(String hashTagName) {
        return hashTagRepository.findByName(hashTagName);
    }

}
