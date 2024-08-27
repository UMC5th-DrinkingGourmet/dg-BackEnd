package com.example.dgbackend.domain.term;

/*
 * 이용약관 종류
 * [필수 약관]
 * TERMS_OF_SERVICE: 서비스 이용 약관
 * ELECTRONIC_FINANCIAL_TRANSACTION: 전자 금융거래 이용 약관
 * PERSONAL_INFORMATION_COLLECT: 개인정보 수집
 *
 * [선택 약관]
 * PERSONAL_INFORMATION_THIRD_PARTY: 개인정보 제3자 제공
 * MARKETING: 마케팅 동의 정책
 */
public enum TermType {
    TERMS_OF_SERVICE, ELECTRONIC_FINANCIAL_TRANSACTION, PERSONAL_INFORMATION_COLLECT, PERSONAL_INFORMATION_THIRD_PARTY, MARKETING
}
