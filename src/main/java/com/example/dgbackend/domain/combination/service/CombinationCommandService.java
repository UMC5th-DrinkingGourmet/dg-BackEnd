package com.example.dgbackend.domain.combination.service;

import com.example.dgbackend.domain.combination.dto.CombinationRequest.WriteCombination;
import com.example.dgbackend.domain.combination.dto.CombinationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CombinationCommandService {

    CombinationResponse.CombinationProcResult uploadCombination(Long recommendId, WriteCombination request);

    CombinationResponse.CombinationProcResult deleteCombination(Long combinationId);

    CombinationResponse.CombinationProcResult editCombination(Long combinationId, WriteCombination request);
}
