package com.example.dgbackend.domain.combinationcomment.repository;

import com.example.dgbackend.domain.combination.domain.Combination;
import com.example.dgbackend.domain.combinationcomment.domain.CombinationComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationCommentRepository extends JpaRepository<CombinationComment, Long> {

    Page<CombinationComment> findAllByCombination(Combination combination, Pageable pageable);
}
