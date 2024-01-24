package com.example.dgbackend.domain.combinationcomment.service;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;

public interface CombinationCommentQueryService {

    CommentPreViewResult getCommentsFromCombination(Long combinationId, Integer page);
}
