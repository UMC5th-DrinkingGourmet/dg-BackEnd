package com.example.dgbackend.domain.recipe.repository;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r WHERE r.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND r.state = :state ORDER BY r.createdAt DESC")
    Page<Recipe> findAllByStateOrderByCreatedAtDesc(boolean state, Pageable pageable,
        Long memberId);

    List<Recipe> findAllByTitleAndMember_Name(String name, String memberName);

    Page<Recipe> findAllByMemberIdAndStateIsTrueOrderByCreatedAtDesc(Long memberId,
        PageRequest pageRequest);

    @Query("SELECT rl.recipe FROM RecipeLike rl WHERE rl.member.id = :memberId AND rl.recipe.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND rl.recipe.state = true AND rl.state = true")
    Page<Recipe> findRecipesByMemberIdAndStateIsTrue(Long memberId, PageRequest pageRequest);

    @Query("SELECT r FROM Recipe r "
        + "WHERE r.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) "
        + "AND (r.title LIKE %:keyword% "
        + "OR r.id IN (SELECT rht.recipe.id FROM RecipeHashTag rht WHERE rht.hashtag.name LIKE %:keyword%)) "
        + "AND r.state = true ORDER BY r.createdAt DESC")
    Page<Recipe> findRecipesByTitleOrHashTagAndStateIsTrueOrderByCreatedAtDesc(String keyword,
        Pageable pageable, Long memberId);

    Page<Recipe> findAllByStateIsTrueOrderByLikeCountDesc(PageRequest pageRequest);

    Optional<Recipe> findByIdAndStateIsTrue(Long id);

    @Query("SELECT r FROM Recipe r WHERE r.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND r.id = :id AND r.state = true")
    Optional<Recipe> findByIdAndMemberAndStateIsTrue(Long id, Long memberId);

    List<Recipe> findAllByMember(Member member);

    @Modifying
    @Query(value = "DELETE FROM recipe WHERE member_id = :id", nativeQuery = true)
    void deleteByMemberIdWithNativeQuery(Long id);
}
