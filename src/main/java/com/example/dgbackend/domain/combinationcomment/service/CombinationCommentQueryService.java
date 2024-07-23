package com.example.dgbackend.domain.combinationcomment.service;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;

import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.member.Member;

public interface CombinationCommentQueryService {

    CommentPreViewResult getCommentsFromCombination(Long combinationId, Integer page,
        Member loginMember);

    CombinationComment getParentComment(Long parentId);

    CombinationComment getComment(Long commentId);

    CombinationComment isDelete(CombinationComment comment);
}
