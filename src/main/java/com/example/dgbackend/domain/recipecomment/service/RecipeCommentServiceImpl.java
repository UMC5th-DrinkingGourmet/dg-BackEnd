package com.example.dgbackend.domain.recipecomment.service;

import static com.example.dgbackend.global.common.MemberValidator.isMatch;

import com.example.dgbackend.domain.enums.State;
import com.example.dgbackend.domain.member.Member;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeCommentServiceImpl implements RecipeCommentService {

	private final RecipeCommentRepository recipeCommentRepository;
	private final RecipeRepository recipeRepository;

	@Override
	public RecipeCommentResponse.RecipeCommentResponseList getRecipeComment(Long recipeId, int page,
		Member loginMember) {

		Recipe recipe = recipeRepository.findById(recipeId)
			.orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));

		Pageable pageable = Pageable.ofSize(10).withPage(page);

		Page<RecipeComment> recipeCommentPage = recipeCommentRepository.findByRecipeAndParentCommentIsNullAndStateIsTrueOrReported(
			recipe, pageable, loginMember.getId());

		return RecipeCommentResponse.toResponseList(recipeCommentPage);
	}

	@Override
	@Transactional
	public RecipeCommentResponse saveRecipeComment(RecipeCommentVO paramVO, Member loginMember) {
		RecipeComment save = recipeCommentRepository.save(getEntity(paramVO, loginMember));

		//댓글 수 증가
		save.getRecipe().changeCommentCount(true);
		return RecipeCommentResponse.toResponse(save);
	}

	@Override
	public RecipeComment getEntity(RecipeCommentVO paramVO, Member loginMember) {
		Recipe recipe = recipeRepository.findById(paramVO.getRecipeId())
			.orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));
		String content = paramVO.getContent();

		return Optional.ofNullable(paramVO.getParentId())

			//대댓글 일 때
			.filter(parentId -> parentId != 0)
			.map(parentId -> RecipeCommentRequest.toEntity(loginMember, recipe, content,
				getParentEntityById(parentId)))

			//댓글 일 때
			.orElse(RecipeCommentRequest.toEntity(loginMember, recipe, content, null));
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
	public RecipeCommentResponse updateRecipeComment(RecipeCommentRequest.Patch requestDTO,
		Member loginMember) {

		RecipeComment recipeComment = getEntityById(requestDTO.getRecipeCommentId());
		if (isMatch(loginMember, recipeComment.getMember())) {
			return RecipeCommentResponse.toResponse(recipeComment.update(requestDTO.getContent()));
		} else {
			throw new ApiException(ErrorStatus._INVALID_MEMBER);
		}
	}

	@Override
	@Transactional
	public RecipeCommentResponse deleteRecipeComment(Long recipeCommentId, Member loginMember) {
		RecipeComment recipeComment = getEntityById(recipeCommentId);

		if (recipeComment.getState().equals(State.FALSE)) {
			throw new ApiException(ErrorStatus._Already_DELETE_RECIPE_COMMENT);
		}

		if (isMatch(loginMember, recipeComment.getMember())) {
			//대댓글도 삭제 -> 대댓글 구현시 수정
			//recipeComment.getChildCommentList().forEach(RecipeComment::delete);
			return RecipeCommentResponse.toResponse(recipeComment.delete());
		} else {
			throw new ApiException(ErrorStatus._INVALID_MEMBER);
		}

	}
}
