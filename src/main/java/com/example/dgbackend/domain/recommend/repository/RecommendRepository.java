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
    
    @Query("SELECT r.imageUrl FROM Recommend r WHERE r.foodName = :foodName AND r.drinkName = :drinkName AND FUNCTION('DATEDIFF', CURRENT_DATE, r.createdAt) < :n AND r.imageUrl NOT IN (SELECT DISTINCT r2.imageUrl FROM Recommend r2 WHERE r2.member.id = :memberId) GROUP BY r.imageUrl")
    List<String> findImageUrlForCaching(Long memberId, String foodName,String drinkName,int n);
}
