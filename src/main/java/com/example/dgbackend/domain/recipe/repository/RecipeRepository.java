package com.example.dgbackend.domain.recipe.repository;

import com.example.dgbackend.domain.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findAllByState(boolean state, Pageable pageable);

    List<Recipe> findAllByTitleAndMember_Name(String name, String memberName);

    Page<Recipe> findAllByMemberIdAndStateIsTrue(Long memberId, PageRequest pageRequest);

    @Query("SELECT rl.recipe FROM RecipeLike rl WHERE rl.member.id = :memberId AND rl.recipe.state = true AND rl.state = true")
    Page<Recipe> findRecipesByMemberIdAndStateIsTrue(Long memberId, PageRequest pageRequest);

    Page<Recipe> findRecipesByTitleContainingAndStateIsTrue(String keyword, Pageable pageable);

    Page<Recipe> findAllByStateIsTrueOrderByLikeCountDesc(PageRequest pageRequest);
}
