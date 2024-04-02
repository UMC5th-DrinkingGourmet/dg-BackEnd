package com.example.dgbackend.domain.enums;

import lombok.Getter;

public enum ReportReason {

	ABUSIVE_LANGUAGE("욕설, 비속어, 혐오 발언 등 타인에게 불쾌감을 주는 내용"), // 욕설, 비속어, 혐오 발언 등 타인에게 불쾌감을 주는 내용
	DEFAMATION("타인을 모욕하거나 명예를 훼손하는 내용"), // 타인을 모욕하거나 명예를 훼손하는 내용
	PORNOGRAPHY_ILLEGAL_CONTENT("음란물, 불법적인 내용"), // 음란물, 불법적인 내용
	UNAUTHORIZED_PERSONAL_INFO("타인의 개인정보를 무단으로 수집하거나 공개"), // 타인의 개인정보를 무단으로 수집하거나 공개
	COPYRIGHT_INFRINGEMENT("타인의 저작권을 침해"), // 타인의 저작권을 침해
	TERMS_VIOLATION("기타 커뮤니티 이용약관에 위반되는 행위"); // 기타 커뮤니티 이용약관에 위반되는 행위

	@Getter
	private final String name;

	ReportReason(String name) {
		this.name = name;
	}
}
