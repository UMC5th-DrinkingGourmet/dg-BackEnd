package com.example.dgbackend.domain.combinationcomment.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.member.Member;

public interface CombinationCommentCommandService {

    CombinationCommentResponse.CommentResult saveCombinationComment(Long combinationId, CombinationCommentRequest.WriteComment request);

    CombinationComment createComment(Combination combination, Member member, CombinationCommentRequest.WriteComment request);

    CombinationComment getParentComment(Long parentId);

    CombinationComment getComment(Long commentId);

    CombinationCommentResponse.CommentProcResult deleteComment(Long commentId);

    CombinationCommentResponse.CommentProcResult updateComment(Long commentId, CombinationCommentRequest.UpdateComment request);

    boolean deleteAllComment(Long memberId);
}
