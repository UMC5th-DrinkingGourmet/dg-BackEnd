package com.example.dgbackend.domain.hashtag.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.hashtag.HashTag;
import com.example.dgbackend.domain.hashtag.repository.HashTagRepository;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class HashTagCommandServiceImpl implements HashTagCommandService {

    private final HashTagRepository hashTagRepository;
    private final HashTagOptionRepository hashTagOptionRepository;

    @Override
    public void uploadHashTag(Combination combination, List<String> hashTageNames) {

        List<HashTag> hashTags = getHashTags(hashTageNames);

        hashTags.forEach(hashTag -> {
            HashTagOption hashTagOption = HashTagOption.builder()
                    .hashTag(hashTag)
                    .combination(combination)
                    .build();

            hashTagOptionRepository.save(hashTagOption);
        });
    }

    @Override
    public List<HashTag> getHashTags(List<String> hashTageNames) {

        List<HashTag> hashTags = hashTageNames.stream()
                .map(ht->{
                    return hashTagRepository.save(getOrCreateHashTag(ht));
                })
                .toList();

        return hashTags;
    }

    private HashTag getOrCreateHashTag(String hashTagName) {
        Optional<HashTag> optionalHashTag = hashTagRepository.findByName(hashTagName);

        return optionalHashTag.orElseGet(() -> HashTag.builder()
                .name(hashTagName)
                .build());
    }
}
