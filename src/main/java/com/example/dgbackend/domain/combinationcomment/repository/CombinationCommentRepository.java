package com.example.dgbackend.domain.combinationcomment.repository;

import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationCommentRepository extends JpaRepository<CombinationComment, Long> {

    Page<CombinationComment> findAllByCombinationIdAndStateIsTrue(Long combinationId,
        Pageable pageable);

    Optional<CombinationComment> findByIdAndStateIsTrue(Long id);
}
