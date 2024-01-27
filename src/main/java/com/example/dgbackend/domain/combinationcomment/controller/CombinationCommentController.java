package com.example.dgbackend.domain.combinationcomment.controller;


import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentRequest;
import com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentCommandService;
import com.example.dgbackend.domain.combinationcomment.service.CombinationCommentQueryService;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.validation.annotation.CheckPage;
import com.example.dgbackend.global.validation.annotation.ExistCombination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.dgbackend.domain.combinationcomment.dto.CombinationCommentResponse.CommentPreViewResult;


@Tag(name = "오늘의 조합 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/combination-comments")
public class CombinationCommentController {

    private final CombinationCommentQueryService combinationCommentQueryService;
    private final CombinationCommentCommandService combinationCommentCommandService;

    @Operation(summary = "특정 오늘의 조합의 댓글 페이징 조회 ", description = "특정 오늘의 조합의 댓글을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 댓글 작성 성공")
    })
    @Parameters({
            @Parameter(name = "combinationId", description = "댓글을 작성하는 오늘의 조합 Id, Path Variable 입니다."),
            @Parameter(name = "page", description = "특정 오늘의 조합의  댓글 목록 페이지 번호, query string 입니다.")
    })
    @GetMapping("/{combinationId}")
    public ApiResponse<CommentPreViewResult> getCombinationComments(@ExistCombination @PathVariable(name = "combinationId") Long combinationId,
                                                                    @CheckPage @RequestParam(name = "page") Integer page) {

        return ApiResponse.onSuccess(combinationCommentQueryService.getCommentsFromCombination(combinationId, page));
    }

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

    @Operation(summary = "오늘의 조합 댓글 삭제", description = "오늘의 조합에 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 댓글 삭제 성공")
    })
    @Parameter(name = "commentId", description = "작성된 댓글 Id, Path Variable 입니다.")
    @DeleteMapping("/{commentId}")
    public ApiResponse<CombinationCommentResponse.CommentProcResult> deleteCombinationComment(@PathVariable(name = "commentId") Long commentId) {

        return ApiResponse.onSuccess(combinationCommentCommandService.deleteComment(commentId));
    }
}
