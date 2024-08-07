package com.example.dgbackend.domain.report;

import com.example.dgbackend.domain.enums.ReportReason;
import com.example.dgbackend.domain.enums.ReportTarget;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.report.dto.ReportRequest.ReportReq;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long resourceId; //신고 대상 id

    @NotNull
    private String content; // 신고 내용

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportTarget reportTarget; // 신고 대상 type

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportReason reportReason; // 신고 사유

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String reportContent; // 신고 대상 내용


    public static Report toEntity(ReportReq reportReq, Member member) {
        return Report.builder()
            .resourceId(reportReq.getResourceId())
            .content(reportReq.getContent())
            .reportTarget(reportReq.getReportTarget())
            .reportReason(reportReq.getReportReason())
            .reportContent(reportReq.getReportContent())
            .member(member)
            .build();
    }

}
