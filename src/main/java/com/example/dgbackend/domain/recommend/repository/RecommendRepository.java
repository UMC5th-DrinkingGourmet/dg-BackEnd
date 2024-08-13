package com.example.dgbackend.domain.recommend.repository;


import com.example.dgbackend.domain.recommend.Recommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    Page<Recommend> findAllByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    List<Recommend> findAllByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
