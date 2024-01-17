package com.example.dgbackend.domain.hashtag.repository;

import com.example.dgbackend.domain.hashtag.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Optional<HashTag> findByName(String name);
    boolean existsByName(String name);
}
