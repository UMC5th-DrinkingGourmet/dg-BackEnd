package com.example.dgbackend.domain.combinationimage.service;


import java.util.List;

public interface CombinationImageQueryService {

    List<String> getCombinationImageUrl(Long combinationId);
}
