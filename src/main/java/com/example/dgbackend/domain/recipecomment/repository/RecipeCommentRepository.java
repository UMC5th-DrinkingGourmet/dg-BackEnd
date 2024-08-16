package com.example.dgbackend.domain.recipecomment.repository;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipecomment.RecipeComment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {

    List<RecipeComment> findAllByRecipe(Recipe recipe);

    Page<RecipeComment> findAllByRecipe(Recipe recipe, Pageable pageable);

    @Query("SELECT r FROM RecipeComment r WHERE r.member NOT IN (SELECT mb.blockedMember FROM MemberBlock mb WHERE mb.member.id = :memberId) AND r.recipe = :recipe AND (r.state = 'TRUE' OR r.state = 'REPORTED')")
    Page<RecipeComment> findByRecipeAndParentCommentIsNullAndStateIsTrueOrReported(Recipe recipe,
        Pageable pageable, Long memberId);

    Optional<RecipeComment> findByIdAndStateIsTrue(Long id);

    @Query("SELECT r FROM RecipeComment r WHERE r.id = :id AND r.state = 'TRUE'")
    Optional<RecipeComment> findByIdAndStateTrue(@Param("id") Long id);

    void deleteAllByRecipe(Recipe recipe);

    @Query("SELECT rc FROM RecipeComment rc WHERE rc.member = :member AND rc.state = 'TRUE'")
    List<RecipeComment> findAllActiveCommentsByMember(@Param("member") Member member);
}
