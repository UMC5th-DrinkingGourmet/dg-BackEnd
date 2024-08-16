package com.example.dgbackend.domain.report.repository;

import com.example.dgbackend.domain.enums.ReportTarget;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.report.Report;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findTopByResourceIdAndReportTargetAndMember(Long resourceId, ReportTarget reportTarget,
        Member member);

    List<Report> findAllByMemberId(Long memberId);

    void deleteAllById(Long reportId);
}
