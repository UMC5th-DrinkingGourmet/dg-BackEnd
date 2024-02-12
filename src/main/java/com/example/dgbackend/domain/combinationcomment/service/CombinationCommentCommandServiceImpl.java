package com.example.dgbackend.domain.combinationcomment.service;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest.WriteComment;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest.toCombinationComment;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentResult;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.toCommentProcResult;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.toCommentResult;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.service.CombinationQueryService;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.combinationcomment.repository.CombinationCommentRepository;
import com.example.dgbackend.domain.member.Member;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CombinationCommentCommandServiceImpl implements CombinationCommentCommandService {

    private final CombinationCommentRepository combinationCommentRepository;
    private final CombinationCommentQueryService combinationCommentQueryService;
    private final CombinationQueryService combinationQueryService;


    @Override
    public CommentResult saveCombinationComment(Member loginMember, Long combinationId,
        WriteComment request) {

        // Combination
        Combination combination = combinationQueryService.getCombination(combinationId);

        // Comment 생성
        CombinationComment newComment = createComment(combination, loginMember, request);

        combination.addCombinationComment(newComment);
        return toCommentResult(newComment);
    }

    @Override
    public CombinationComment createComment(Combination combination, Member member,
        WriteComment request) {

        String content = request.getContent();

        CombinationComment newComment = Optional.ofNullable(request.getParentId())
            .filter(parentId -> parentId != 0)
            .map(parentId -> toCombinationComment(combination, member, content,
                combinationCommentQueryService.getParentComment(request.getParentId())))
            .orElse(toCombinationComment(combination, member, content, null));

        return combinationCommentRepository.save(newComment);
    }

    @Override
    public CombinationCommentResponse.CommentProcResult deleteComment(Long commentId) {

        CombinationComment combinationComment = combinationCommentQueryService.getComment(
            commentId);
        combinationComment.deleteComment();

        Optional.ofNullable(combinationComment.getChildComments())
            .ifPresent(child -> child.forEach(CombinationComment::deleteComment));
        return toCommentProcResult(commentId);
    }

    @Override
    public CombinationCommentResponse.CommentResult updateComment(Long commentId,
        CombinationCommentRequest.UpdateComment request) {

        CombinationComment combinationComment = combinationCommentQueryService.getComment(
            commentId);

        combinationComment.updateComment(request.getContent());

        return toCommentResult(combinationComment);
    }

}
