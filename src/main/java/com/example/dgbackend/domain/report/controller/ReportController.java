package com.example.dgbackend.domain.report.controller;


import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.report.dto.ReportRequest.ReportReq;
import com.example.dgbackend.domain.report.service.ReportServiceImpl;
import com.example.dgbackend.global.common.response.ApiResponse;
import com.example.dgbackend.global.jwt.annotation.MemberObject;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportServiceImpl reportService;

	@PostMapping("/member/reports")
	public ApiResponse<String> report(@MemberObject Member member, @RequestBody ReportReq reportReq)
		throws MessagingException {
		return ApiResponse.onSuccess(reportService.report(reportReq, member));
	}


}
