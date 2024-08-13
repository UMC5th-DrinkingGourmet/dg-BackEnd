package com.example.dgbackend.domain.recipelike.repository;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipelike.RecipeLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {

    Optional<RecipeLike> findByRecipeAndMember(Recipe recipe, Member member);

    void deleteAllByRecipeId(Long recipeId);

    void deleteAllByMember(Member member);
}
