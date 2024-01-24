package com.example.dgbackend.domain.combinationcomment.service;

import com.example.dgbackend.domain.combinationcomment.repository.CombinationCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;
import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.toCommentPreViewResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CombinationCommentQueryServiceImpl implements CombinationCommentQueryService {

    private final CombinationCommentRepository combinationCommentRepository;

    @Override
    public CommentPreViewResult getCommentsFromCombination(Long combinationId, Integer page) {

        return toCommentPreViewResult(combinationCommentRepository.findAllByCombinationId(combinationId, PageRequest.of(page, 10)));
    }
}
