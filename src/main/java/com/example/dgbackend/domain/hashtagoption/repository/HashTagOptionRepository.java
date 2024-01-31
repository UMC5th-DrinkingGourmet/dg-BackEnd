package com.example.dgbackend.domain.hashtagoption.repository;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.hashtagoption.HashTagOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashTagOptionRepository extends JpaRepository<HashTagOption, Long> {

    @Query("SELECT hto FROM HashTagOption hto JOIN FETCH hto.hashTag WHERE hto.combination = :combination")
    List<HashTagOption> findAllByCombinationWithFetch(@Param("combination") Combination combination);

    void deleteByCombinationId(Long combinationId);

    HashTagOption findByCombinationAndHashTagName(Combination combination, String name);

}
