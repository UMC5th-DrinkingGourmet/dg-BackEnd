package com.example.dgbackend.domain.combinationcomment.service;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;

import com.example.dgbackend.domain.combinationcomment.CombinationComment;

public interface CombinationCommentQueryService {

    CommentPreViewResult getCommentsFromCombination(Long combinationId, Integer page);

    CombinationComment getParentComment(Long parentId);

    CombinationComment getComment(Long commentId);

    CombinationComment isDelete(CombinationComment comment);
}
