package com.example.dgbackend.domain.combinationcomment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class CombinationCommentRequest {

    @Getter
    @Schema(name = "오늘의 조합 댓글 작성 요청 DTO")
    public static class WriteComment {

        private String content;
        private Long parentId;
    }
}
