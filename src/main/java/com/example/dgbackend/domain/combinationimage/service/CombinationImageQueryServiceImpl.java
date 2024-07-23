package com.example.dgbackend.domain.combinationimage.service;

import com.example.dgbackend.domain.combinationimage.CombinationImage;
import com.example.dgbackend.domain.combinationimage.repository.CombinationImageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationImageQueryServiceImpl implements CombinationImageQueryService {

    private final CombinationImageRepository combinationImageRepository;

    @Override
    public List<String> getCombinationImageUrl(Long combinationId) {

        List<CombinationImage> combinationImages = combinationImageRepository.findAllByCombinationId(
            combinationId);

        return combinationImages.stream()
            .map(CombinationImage::getImageUrl)
            .collect(Collectors.toList());
    }
}
