package com.example.dgbackend.domain.recipe.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findAllByState(boolean state, Pageable pageable);

    List<Recipe> findAllByNameAndMember_Name(String name, String memberName);

    Page<Recipe> findAllByMemberId(Long memberId, PageRequest pageRequest);

    @Query("SELECT rl.recipe FROM RecipeLike rl WHERE rl.member.id = :memberId")
    Page<Recipe> findRecipesByMemberId(Long memberId, PageRequest pageRequest);

    List<Recipe> findRecipesByNameContaining(String keyword);
}
