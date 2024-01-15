package com.example.dgbackend.domain.combinationcomment.controller;

import com.example.dgbackend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "오늘의 조합 댓글 관련 API")
@RestController
@RequestMapping("/api/combinations{combinationId}/combinationComments")
@RequiredArgsConstructor
public class CombinationCommentController {

//    @Operation(summary = "오늘의 조합에 작성된 댓글 조회", description = "특정 오늘의 조합의 추가 댓글을 조회합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 댓글 조회 성공")
//    })
//    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
//    @GetMapping("")
//    public ApiResponse<> getCombinationComments(@PathVariable(name = "combinationId") Long combinationId) {
//
//        return null;
//    }
//
//    @Operation(summary = "오늘의 조합에 댓글 작성", description = "특정 오늘의 조합에 댓글을 작성합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 댓글 작성 성공")
//    })
//    @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다.")
//    @PostMapping("")
//    public ApiResponse<> writeCombinationComments(@PathVariable(name = "combinationId") Long combinationId) {
//
//        return null;
//    }
//
//    @Operation(summary = "오늘의 조합에 작성된 댓글 수정", description = "특정 오늘의 조합에 댓글을 수정합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 댓글 수정 성공")
//    })
//    @Parameters({
//            @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다."),
//            @Parameter(name = "combinationCommentId", description = "댓글 Id, Path Variable 입니다.")
//    })
//    @PatchMapping("{combinationCommentId}")
//    public ApiResponse<> editCombinationComments(@PathVariable(name = "combinationId") Long combinationId,
//                                                  @PathVariable(name = "combinationCommentId") Long combinationCommentId) {
//
//        return null;
//    }
//
//    @Operation(summary = "오늘의 조합에 작성된 댓글 삭제", description = "특정 오늘의 조합에 댓글을 삭제합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "오늘의 조합 댓글 삭제 성공")
//    })
//    @Parameters({
//            @Parameter(name = "combinationId", description = "오늘의 조합 Id, Path Variable 입니다."),
//            @Parameter(name = "combinationCommentId", description = "댓글 Id, Path Variable 입니다.")
//    })
//    @DeleteMapping("{combinationCommentId}")
//    public ApiResponse<> deleteCombinationComments(@PathVariable(name = "combinationId") Long combinationId,
//                                                 @PathVariable(name = "combinationCommentId") Long combinationCommentId) {
//
//        return null;
//    }
}
