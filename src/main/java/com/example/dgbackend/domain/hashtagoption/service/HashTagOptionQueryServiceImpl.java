package com.example.dgbackend.domain.hashtagoption.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashTagOptionQueryServiceImpl implements HashTagOptionQueryService {

    private final HashTagOptionRepository hashTagOptionRepository;

    @Override
    public List<HashTagOption> getAllHashTagOptionByCombination(Combination combination) {
        return hashTagOptionRepository.findAllByCombinationWithFetch(combination);
    }
}
