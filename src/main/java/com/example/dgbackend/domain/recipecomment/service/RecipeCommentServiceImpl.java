package com.example.dgbackend.domain.recipecomment.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.service.MemberService;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.repository.RecipeRepository;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentRequest;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentResponse;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentVO;
import com.example.dgbackend.domain.recipecomment.repository.RecipeCommentRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeCommentServiceImpl implements RecipeCommentService {

    private final RecipeCommentRepository recipeCommentRepository;
    private final MemberService memberService;
    private final RecipeRepository recipeRepository;

    @Override
    public RecipeCommentResponse.RecipeCommentResponseList getRecipeComment(Long recipeId, int page) {

        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));

        Pageable pageable = Pageable.ofSize(10).withPage(page);

        Page<RecipeComment> recipeCommentPage = recipeCommentRepository.findByRecipeAndParentCommentIsNullAndStateIsTrue(recipe, pageable);

        return RecipeCommentResponse.toResponseList(recipeCommentPage);
    }

    @Override
    @Transactional
    public RecipeCommentResponse saveRecipeComment(RecipeCommentVO paramVO) {
        RecipeComment save = recipeCommentRepository.save(getEntity(paramVO));

        //댓글 수 증가
        save.getRecipe().changeCommentCount(true);
        return RecipeCommentResponse.toResponse(save);
    }

    @Override
    public RecipeComment getEntity(RecipeCommentVO paramVO) {
        Member member = memberService.findMemberByName(paramVO.getMemberName());
        Recipe recipe = recipeRepository.findById(paramVO.getRecipeId()).orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));
        String content = paramVO.getContent();

        return Optional.ofNullable(paramVO.getParentId())

                //대댓글 일 때
                .filter(parentId -> parentId != 0)
                .map(parentId -> RecipeCommentRequest.toEntity(member, recipe, content, getParentEntityById(parentId)))

                //댓글 일 때
                .orElse(RecipeCommentRequest.toEntity(member, recipe, content, null));
    }

    @Override
    public RecipeComment getParentEntityById(Long parentId) {
        RecipeComment ParentRecipeComment = getEntityById(parentId);

        //부모의 부모 댓글이 존재할 경우
        if (ParentRecipeComment.getParentComment() != null) {
            throw new ApiException(ErrorStatus._OVER_DEPTH_RECIPE_COMMENT);
        }

        return ParentRecipeComment;
    }

    @Override
    public RecipeComment getEntityById(Long id) {
        return recipeCommentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE_COMMENT));
    }

    @Override
    @Transactional
    public RecipeCommentResponse updateRecipeComment(RecipeCommentRequest.Patch requestDTO) {
        RecipeComment recipeComment = getEntityById(requestDTO.getRecipeCommentId()).update(requestDTO.getContent());
        return RecipeCommentResponse.toResponse(recipeComment);
    }

    @Override
    @Transactional
    public RecipeCommentResponse deleteRecipeComment(Long recipeCommentId) {
        RecipeComment entityById = getEntityById(recipeCommentId);

        if (!entityById.isState()) {
            throw new ApiException(ErrorStatus._Already_DELETE_RECIPE_COMMENT);
        }

        RecipeComment recipeComment = entityById.delete();

        //대댓글도 삭제
        recipeComment.getChildCommentList().forEach(RecipeComment::delete);
        return RecipeCommentResponse.toResponse(recipeComment);
    }
}
