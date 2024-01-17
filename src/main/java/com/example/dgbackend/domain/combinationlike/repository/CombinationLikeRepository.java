package com.example.dgbackend.domain.combinationlike.repository;

import com.example.dgbackend.domain.combinationlike.domain.CombinationLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationLikeRepository extends JpaRepository<CombinationLike, Long> {

    void deleteByCombinationId(Long combinationId);
}
