package com.example.dgbackend.domain.combination.repository;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.member.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CombinationRepository extends JpaRepository<Combination, Long> {

    Page<Combination> findAllByMemberIdAndStateIsTrue(Long memberId, PageRequest pageRequest);

    List<Combination> findAllByMember(Member member);

    Page<Combination> findAllByState(Boolean state, PageRequest pageRequest);

    Boolean existsByIdAndMember(Long combinationId, Member member);

    Page<Combination> findCombinationsByLikeCountGreaterThanEqualAndStateIsTrueOrderByCreatedAtDesc(
        Long likeCount, PageRequest pageRequest);

    @Query("SELECT cl.combination FROM CombinationLike cl WHERE cl.member.id = :memberId AND cl.combination.state = true")
    Page<Combination> findCombinationsByMemberIdAndStateIsTrue(Long memberId, PageRequest pageRequest);

    Page<Combination> findCombinationsByTitleContainingAndStateIsTrue(String keyword, PageRequest pageRequest);

    Page<Combination> findCombinationsByTitleContainingAndLikeCountGreaterThanEqualAndStateIsTrueOrderByCreatedAtDesc(
        String keyword, PageRequest pageRequest, Long likeCount);

    @Query("SELECT c FROM Combination c WHERE c.likeCount >= 30 AND c.state = true ORDER BY RAND() LIMIT 5")
    List<Combination> findCombinationsByLikeCountGreaterThanEqualAndStateIsTrue();

    Page<Combination> findAllByStateIsTrueOrderByLikeCountDesc(PageRequest pageRequest);

    boolean existsByIdAndState(Long combinationId, boolean state);
}
