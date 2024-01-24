package com.example.dgbackend.domain.recipecomment.service;

import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.service.MemberService;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.service.RecipeService;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentRequest;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentResponse;
import com.example.dgbackend.domain.recipecomment.dto.RecipeCommentVO;
import com.example.dgbackend.domain.recipecomment.repository.RecipeCommentRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeCommentServiceImpl implements RecipeCommentService{

    private final RecipeCommentRepository recipeCommentRepository;
    private final MemberService memberService;
    private final RecipeService recipeService;

    @Override
    public List<RecipeCommentResponse> getRecipeComment(Long recipeId) {

        Recipe recipe = recipeService.getRecipe(recipeId);

        return recipeCommentRepository.findAllByRecipe(recipe).stream()
                .filter(RecipeComment::isState)
                .filter(recipeComment -> recipeComment.getParentComment() == null)
                .map(RecipeCommentResponse::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RecipeCommentResponse saveRecipeComment(RecipeCommentVO paramVO) {
        return RecipeCommentResponse.toResponse(recipeCommentRepository.save(getEntity(paramVO)));
    }

    @Override
    public RecipeComment getEntity(RecipeCommentVO paramVO) {
        Member member = memberService.findMemberByName(paramVO.getMemberName());
        Recipe recipe = recipeService.getRecipe(paramVO.getRecipeId());
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
        if(ParentRecipeComment.getParentComment() != null) {
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
        RecipeComment recipeComment = getEntityById(recipeCommentId).delete();

        //대댓글도 삭제
        recipeComment.getChildCommentList().forEach(RecipeComment::delete);
        return RecipeCommentResponse.toResponse(recipeComment);
    }

}
