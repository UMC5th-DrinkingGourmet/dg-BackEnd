package com.example.dgbackend.domain.hashtag.repository;

import com.example.dgbackend.domain.hashtag.HashTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Optional<HashTag> findByName(String name);
}
