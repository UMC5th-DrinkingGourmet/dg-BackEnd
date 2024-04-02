package com.example.dgbackend.domain.report;

import com.example.dgbackend.domain.enums.ReportReason;
import com.example.dgbackend.domain.enums.ReportTarget;
import com.example.dgbackend.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private Long resourceId;

	@NotNull
	private String content;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ReportTarget reportTarget; // 신고 대상 type

	@NotNull
	@Enumerated(EnumType.STRING)
	private ReportReason reportReason; // 신고 사유

	@ManyToOne
	private Member member;

}
