package com.example.dgbackend.domain.term;

/*
    * 이용약관 종류
    * [필수 약관]
    * PRIVACY: 사용자 데이터 및 개인정보 보호 정책
    * CONTENT: 사용자 콘텐츠 정책
    * AGE: 연령 제한 정책
    * COPYRIGHT: 저작권 및 상표 정책
    * SERVICE_CHANGE: 서비스 변경 및 중단 정책
    *
    * [선택 약관]
    * MARKETING: 마케팅 동의 정책
 */
public enum TermType {
    PRIVACY, CONTENT, AGE, COPYRIGHT, SERVICE_CHANGE, MARKETING
}
