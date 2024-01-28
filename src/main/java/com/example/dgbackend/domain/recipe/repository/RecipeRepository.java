package com.example.dgbackend.domain.recipe.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByState(boolean state);
    List<Recipe> findAllByNameAndMember_Name(String name, String memberName);
    Page<Recipe> findAllByMemberId(Long memberId, PageRequest pageRequest);
}
