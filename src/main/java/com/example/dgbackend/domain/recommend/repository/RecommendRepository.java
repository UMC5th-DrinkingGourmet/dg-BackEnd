package com.example.dgbackend.domain.recommend.repository;

import com.example.dgbackend.domain.recommend.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}
