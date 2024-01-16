package com.example.dgbackend.domain.hashtag.service;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.hashtag.HashTag;
import com.example.dgbackend.domain.hashtag.repository.HashTagRepository;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HashTagCommandServiceImpl implements HashTagCommandService{

    private final HashTagRepository hashTagRepository;

    @Override
    public void uploadHashTag(Combination combination, List<String> hashTageNames) {

        List<HashTag> hashTags = hashTageNames.stream()
                .map(ht -> HashTag.builder()
                        .name(ht)
                        .hashTagOptionList(new ArrayList<>())
                        .build())
                .toList();

        hashTags.forEach(hashTag -> {
            HashTagOption hashTagOption = HashTagOption.builder()
                    .hashTag(hashTag)
                    .combination(combination)
                    .build();

            hashTag.addHashTagOption(hashTagOption);
            hashTagRepository.save(hashTag);
        });
    }
}
