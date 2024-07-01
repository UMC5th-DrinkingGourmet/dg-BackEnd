package com.example.dgbackend.domain.combinationcomment.service;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.toCommentPreViewResult;

import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.repository.CombinationCommentRepository;
import com.example.dgbackend.domain.enums.State;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationCommentQueryServiceImpl implements CombinationCommentQueryService {

	private final CombinationCommentRepository combinationCommentRepository;

	@Override
	public CommentPreViewResult getCommentsFromCombination(Long combinationId, Integer page,
		Member loginMember) {

		Page<CombinationComment> comments = combinationCommentRepository.findByCombinationIdAndStateTrueOrReported(
			combinationId, PageRequest.of(page, 10), loginMember.getId());

		return toCommentPreViewResult(comments);

	}

	@Override
	public CombinationComment getParentComment(Long parentId) {
		CombinationComment parentComment = getComment(parentId);

		// 부모의 부모 댓글이 존재할 경우 => 에러 핸들러 실행 (대댓글까지 가능)
		if (parentComment.getParentComment() != null) {
			throw new ApiException(ErrorStatus._OVER_DEPTH_COMBINATION_COMMENT);
		}
		return parentComment;
	}

	@Override
	public CombinationComment getComment(Long commentId) {
		CombinationComment comment = combinationCommentRepository.findById(commentId)
			.orElseThrow(() -> new ApiException(ErrorStatus._COMBINATION_COMMENT_NOT_FOUND));
		return isDelete(comment);

	}

	@Override
	public CombinationComment isDelete(CombinationComment comment) {

		if (comment.getState().equals(State.FALSE)) {
			throw new ApiException(ErrorStatus._DELETE_COMBINATION_COMMENT);
		}
		return comment;
	}
}
