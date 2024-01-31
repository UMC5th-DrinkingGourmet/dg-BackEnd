package com.example.dgbackend.domain.combinationimage.service;


import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationimage.dto.CombinationImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CombinationImageCommandService {

    void updateCombinationImage(Combination combination, List<String> combinationImageList);

    CombinationImageResponse.CombinationImageResult uploadImage(List<MultipartFile> multipartFiles);
}
