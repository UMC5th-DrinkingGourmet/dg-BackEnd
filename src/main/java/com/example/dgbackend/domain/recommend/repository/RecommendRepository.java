package com.example.dgbackend.domain.recommend.repository;


import com.example.dgbackend.domain.recommend.Recommend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    Page<Recommend> findAllByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    List<Recommend> findAllByMemberId(Long memberId);

    @Modifying
    @Query(value = "DELETE FROM recommend WHERE member_id = :memberId", nativeQuery = true)
    void deleteAllByMemberIdWithNativeQuery(Long memberId);

    List<Recommend> findAllByFoodNameAndDrinkNameAndMemberId(String foodName, String drinkName, Long memberId);
}
