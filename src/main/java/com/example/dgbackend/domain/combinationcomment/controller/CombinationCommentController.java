package com.example.dgbackend.domain.combinationcomment.controller;

import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentCommandService;
import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "오늘의 조합 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/combination-comments")
public class CombinationCommentController {

    private final CombinationCommentCommandService combinationCommentCommandService;

    @Operation(summary = "오늘의 조합 댓글 작성", description = "오늘의 조합에 댓글을 작성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 댓글 작성 성공")
    })
    @Parameter(name = "combinationId", description = "작성하는 오늘의 조합 Id, Path Variable 입니다.")
    @PostMapping("/{combinationId}")
    public ApiResponse<CombinationCommentResponse.CommentResult> saveCombinationComment(@PathVariable(name = "combinationId") Long combinationId,
                                                                                        @RequestBody CombinationCommentRequest.WriteComment request) {

        // TODO: HttpServletRequest 사용해서 Authorization token으로 사용자 정보 받아오기
        return ApiResponse.onSuccess(combinationCommentCommandService.saveCombinationComment(combinationId, request));
    }
}
