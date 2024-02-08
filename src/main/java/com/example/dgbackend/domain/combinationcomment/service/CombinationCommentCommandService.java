package com.example.dgbackend.domain.combinationcomment.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.member.Member;

public interface CombinationCommentCommandService {

    CombinationCommentResponse.CommentResult saveCombinationComment(Member loginMember,
        Long combinationId, CombinationCommentRequest.WriteComment request);

    CombinationComment createComment(Combination combination, Member member,
        CombinationCommentRequest.WriteComment request);

    CombinationCommentResponse.CommentProcResult deleteComment(Long commentId);

    CombinationCommentResponse.CommentResult updateComment(Long commentId,
        CombinationCommentRequest.UpdateComment request);

    boolean deleteAllComment(Long memberId);
}
