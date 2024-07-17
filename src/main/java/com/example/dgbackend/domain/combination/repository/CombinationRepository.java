package com.example.dgbackend.domain.combination.repository;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CombinationRepository extends JpaRepository<Combination, Long> {

	Page<Combination> findAllByMemberIdAndStateIsTrueOrderByCreatedAtDesc(Long memberId,
		PageRequest pageRequest);

	List<Combination> findAllByMember(Member member);

	@Query("SELECT c FROM Combination c WHERE c.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND c.state = :state ORDER BY c.createdAt DESC")
	Page<Combination> findAllByStateOrderByCreatedAtDesc(Boolean state, PageRequest pageRequest,
		Long memberId);

	Boolean existsByIdAndMember(Long combinationId, Member member);

	@Query("SELECT c FROM Combination c WHERE c.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND c.likeCount >= :likeCount AND c.state = true ORDER BY c.createdAt DESC")
	Page<Combination> findCombinationsByLikeCountGreaterThanEqualAndStateIsTrueOrderByCreatedAtDesc(
		Long likeCount, PageRequest pageRequest, Long memberId);

	@Query("SELECT cl.combination FROM CombinationLike cl WHERE cl.combination.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND cl.member.id = :memberId AND cl.combination.state = true AND cl.state = true")
	Page<Combination> findCombinationsByMemberIdAndStateIsTrue(Long memberId,
		PageRequest pageRequest);

	@Query("SELECT c FROM Combination c WHERE c.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND c.title LIKE %:keyword% AND c.state = true ORDER BY c.createdAt DESC")
	Page<Combination> findCombinationsByTitleContainingAndStateIsTrueOrderByCreatedAtDesc(
		String keyword, PageRequest pageRequest, Long memberId);


	@Query("SELECT c FROM Combination c WHERE c.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND c.title LIKE %:keyword% AND c.likeCount >= :likeCount AND c.state = true ORDER BY c.createdAt DESC")
	Page<Combination> findCombinationsByTitleContainingAndLikeCountGreaterThanEqualAndStateIsTrueOrderByCreatedAtDesc(
		String keyword, PageRequest pageRequest, Long likeCount, Long memberId);

    @Query("SELECT c FROM Combination c WHERE c.likeCount >= 30 AND c.state = true ORDER BY RAND() LIMIT 5")
    List<Combination> findCombinationsByLikeCountGreaterThanEqualAndStateIsTrue();

    Page<Combination> findAllByStateIsTrueOrderByLikeCountDesc(PageRequest pageRequest);

	boolean existsByIdAndState(Long combinationId, boolean state);

	Optional<Combination> findByIdAndStateIsTrue(Long id);

	@Query("SELECT c FROM Combination c WHERE c.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND c.id = :id AND c.state = true")
	Optional<Combination> findCombinationByIdAndStateIsTrue(Long id, Long memberId);
}
