package com.example.dgbackend.domain.combinationcomment.repository;

import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CombinationCommentRepository extends JpaRepository<CombinationComment, Long> {

	@Query("SELECT c FROM CombinationComment c WHERE c.combination.id = :combinationId AND (c.state = 'TRUE' OR c.state = 'REPORTED')")
	Page<CombinationComment> findByCombinationIdAndStateTrueOrReported(@Param("combinationId") Long combinationId, Pageable pageable);

	Optional<CombinationComment> findByIdAndStateIsTrue(Long id);
}
