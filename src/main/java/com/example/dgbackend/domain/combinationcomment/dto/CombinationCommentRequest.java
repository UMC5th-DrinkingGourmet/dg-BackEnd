package com.example.dgbackend.domain.combinationcomment.dto;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.enums.State;
import com.example.dgbackend.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class CombinationCommentRequest {

    @Getter
    @Schema(name = "오늘의 조합 댓글 작성 요청 DTO")
    public static class WriteComment {

        private String content;
        private Long parentId;
    }

    public static CombinationComment toCombinationComment(Combination combination, Member member,
        String content, CombinationComment parentComment) {

        return CombinationComment.builder()
            .content(content)
            .parentComment(parentComment)
            .member(member)
            .combination(combination)
            .state(State.TRUE)
            .build();
    }

    @Getter
    @Schema(name = "오늘의 조합 댓글 수정 요청 DTO")
    public static class UpdateComment {

        private String content;
    }

}
