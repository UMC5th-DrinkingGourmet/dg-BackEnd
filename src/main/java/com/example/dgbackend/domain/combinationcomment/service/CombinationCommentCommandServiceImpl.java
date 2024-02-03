package com.example.dgbackend.domain.combinationcomment.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.combinationcomment.repository.CombinationCommentRepository;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest.WriteComment;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest.toCombinationComment;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CombinationCommentCommandServiceImpl implements CombinationCommentCommandService {

    private final MemberRepository memberRepository;
    private final CombinationCommentRepository combinationCommentRepository;
    private final CombinationQueryService combinationQueryService;


    @Override
    public CommentResult saveCombinationComment(Long combinationId, WriteComment request) {

        // Combination
        Combination combination = combinationQueryService.getCombination(combinationId);

        // TODO: Login Member는 Token 정보 조회로 변경
        Member loginMember = memberRepository.findById(1L).get();

        // Comment 생성
        CombinationComment newComment = createComment(combination, loginMember, request);

        combination.addCombinationComment(newComment);
        return toCommentResult(newComment);
    }

    @Override
    public CombinationComment createComment(Combination combination, Member member, WriteComment request) {

        String content = request.getContent();

        CombinationComment newComment = Optional.ofNullable(request.getParentId())
                .filter(parentId -> parentId != 0)
                .map(parentId -> toCombinationComment(combination, member, content, getParentComment(request.getParentId())))
                .orElse(toCombinationComment(combination, member, content, null));

        return combinationCommentRepository.save(newComment);
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
        return combinationCommentRepository.findById(commentId).orElseThrow(
                () -> new ApiException(ErrorStatus._COMBINATION_COMMENT_NOT_FOUND)
        );
    }

    @Override
    public CombinationCommentResponse.CommentProcResult deleteComment(Long commentId) {

        CombinationComment combinationComment = getComment(commentId);
        combinationComment.deleteComment();

        Optional.ofNullable(combinationComment.getChildComments())
                .ifPresent(child -> child.forEach(CombinationComment::deleteComment));
        return toCommentProcResult(commentId);
    }

    @Override
    public CombinationCommentResponse.CommentProcResult updateComment(Long commentId, CombinationCommentRequest.UpdateComment request) {

        CombinationComment combinationComment = getComment(commentId);

        combinationComment.updateComment(request.getContent());

        return toCommentProcResult(commentId);
    }
}
