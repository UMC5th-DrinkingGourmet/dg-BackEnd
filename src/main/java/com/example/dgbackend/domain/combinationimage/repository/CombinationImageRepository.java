package com.example.dgbackend.domain.combinationimage.repository;

import com.example.dgbackend.domain.combinationimage.CombinationImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CombinationImageRepository extends JpaRepository<CombinationImage, Long> {

     List<CombinationImage> findAllByCombinationId(Long combinationId);
}
