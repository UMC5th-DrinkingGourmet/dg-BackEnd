package com.example.dgbackend.domain.report.service;


import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.report.dto.ReportRequest.ReportReq;
import jakarta.mail.MessagingException;

public interface ReportService {

    String report(ReportReq reportReq, Member member) throws MessagingException;

    void deleteReport(Long memberId);

}
