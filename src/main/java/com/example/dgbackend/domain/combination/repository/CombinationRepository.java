package com.example.dgbackend.domain.combination.repository;

import com.example.dgbackend.domain.combination.domain.Combination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationRepository extends JpaRepository<Combination, Long> {

}
