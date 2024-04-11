package com.example.dgbackend.domain.report.dto;

import com.example.dgbackend.domain.enums.ReportReason;
import com.example.dgbackend.domain.enums.ReportTarget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportRequest {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReportReq {

		Long resourceId; // 신고 대상 id
		ReportTarget reportTarget; // 신고 대상 타입
		ReportReason reportReason; // 신고 사유
		String content; // 신고 사유(내용)
		String reportContent; // 신고 대상 내용
		Long reportedMemberId; // 신고당한 멤버 id
	}

}
