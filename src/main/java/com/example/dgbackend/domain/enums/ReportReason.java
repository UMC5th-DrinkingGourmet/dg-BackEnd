package com.example.dgbackend.domain.enums;

import lombok.Getter;

public enum ReportReason {

	ABUSIVE_LANGUAGE("욕설, 비속어, 혐오 발언 등 타인에게 불쾌감을 주는 내용" , "1일 정지"),
	DEFAMATION("타인을 모욕하거나 명예를 훼손하는 내용", "7일 정지"),
	PORNOGRAPHY_ILLEGAL_CONTENT("음란물, 불법적인 내용" , "30일 정지"),
	UNAUTHORIZED_PERSONAL_INFO("타인의 개인정보를 무단으로 수집하거나 공개", "영구 정지"),
	COPYRIGHT_INFRINGEMENT("타인의 저작권을 침해", "영구 정지"),
	TERMS_VIOLATION("기타 커뮤니티 이용약관에 위반되는 행위" , "임시 정지");

	@Getter
	private final String name;

	@Getter
	private final String suspensionDuration;

	ReportReason(String name, String suspensionDuration) {
		this.name = name;
		this.suspensionDuration = suspensionDuration;
	}}
