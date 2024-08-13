package com.example.dgbackend.domain.cancellation.controller;

import com.example.dgbackend.domain.cancellation.service.CancellationSchedular;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "탈퇴 테스트용 API")
@RestController
@Validated
@RequestMapping("/cancellations")
@RequiredArgsConstructor
public class CancellationController {

    private final CancellationSchedular cancellationSchedular;

    @Operation(summary = "탈퇴 시험용 API")
    @DeleteMapping("/{cancellationId}")
    public ApiResponse<Boolean> getCombinations(@Parameter(hidden = true) @MemberObject Member loginMember, @PathVariable(name = "cancellationId") Long cancellationId) {
        return ApiResponse.onSuccess(cancellationSchedular.runCancellation(cancellationId));
    }
}
