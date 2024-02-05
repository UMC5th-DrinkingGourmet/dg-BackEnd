package com.example.dgbackend.domain.recipe.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByState(boolean state);
    List<Recipe> findAllByNameAndMember_Name(String name, String memberName);
    Page<Recipe> findAllByMemberId(Long memberId, PageRequest pageRequest);
    boolean deleteAllByMemberId(Long memberId);
    @Query("SELECT rl.recipe FROM RecipeLike rl WHERE rl.member.id = :memberId")
    Page<Recipe> findRecipesByMemberId(Long memberId, PageRequest pageRequest);
}
