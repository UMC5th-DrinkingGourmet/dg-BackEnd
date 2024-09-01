package com.example.dgbackend.domain.report.controller;


import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.report.dto.ReportRequest.ReportReq;
import com.example.dgbackend.domain.report.service.ReportServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportServiceImpl reportService;

    @Operation(summary = "신고하기", description = "신고 가능한 항목들과 제재 조치(reportReason):\n" +
        "- ABUSIVE_LANGUAGE: 욕설, 비속어, 혐오 발언 등 타인에게 불쾌감을 주는 내용 (1일 정지)\n" +
        "- DEFAMATION: 타인을 모욕하거나 명예를 훼손하는 내용 (7일 정지)\n" +
        "- PORNOGRAPHY_ILLEGAL_CONTENT: 음란물, 불법적인 내용 (30일 정지)\n" +
        "- UNAUTHORIZED_PERSONAL_INFO: 타인의 개인정보를 무단으로 수집하거나 공개 (영구 정지)\n" +
        "- COPYRIGHT_INFRINGEMENT: 타인의 저작권을 침해 (영구 정지)\n" +
        "- TERMS_VIOLATION: 기타 커뮤니티 이용약관에 위반되는 행위 (임시 정지)\n\n" +
        "신고 대상(reportTarget):\n" +
        "- COMBINATION: 조합글\n" +
        "- RECIPE: 레시피글\n" +
        "- COMBINATION_COMMENT: 조합 댓글\n" +
        "- RECIPE_COMMENT: 레시피 댓글")
    @PostMapping("/member/reports")
    public ApiResponse<String> report(@MemberObject Member member, @RequestBody ReportReq reportReq)
        throws MessagingException {
        return ApiResponse.onSuccess(reportService.report(reportReq, member));
    }


}
