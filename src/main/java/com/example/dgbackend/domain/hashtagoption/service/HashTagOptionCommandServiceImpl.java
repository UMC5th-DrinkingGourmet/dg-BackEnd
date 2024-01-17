package com.example.dgbackend.domain.hashtagoption.service;

import com.example.dgbackend.domain.hashtagoption.repository.HashTagOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashTagOptionCommandServiceImpl implements HashTagOptionCommandService {

    private final HashTagOptionRepository hashTagOptionRepository;

    @Override
    public void deleteHashTagOption(Long combinationId) {
        hashTagOptionRepository.deleteByCombinationId(combinationId);
    }
}
