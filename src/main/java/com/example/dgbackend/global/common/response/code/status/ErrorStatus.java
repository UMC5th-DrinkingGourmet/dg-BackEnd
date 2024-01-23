package com.example.dgbackend.global.common.response.code.status;

import com.example.dgbackend.global.common.response.code.BaseErrorCode;
import com.example.dgbackend.global.common.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    //일반 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //멤버 관련
    _EMPTY_MEMBER(HttpStatus.CONFLICT, "MEMBER_001", "존재하지 않는 사용자입니다."),

    //오늘의 조합 관련
    _COMBINATION_NOT_FOUND(HttpStatus.NOT_FOUND, "COMBINATION_001", "존재하지 않는 오늘의 조합입니다."),

    //오늘의 조합 댓글
    _COMBINATION_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMBINATION_COMMENT_001", "존재하지 않는 오늘의 조합 댓글입니다."),
    _OVER_DEPTH_COMBINATION_COMMENT(HttpStatus.BAD_REQUEST, "COMBINATION_COMMENT_002", "대댓글까지만 가능합니다."),

    //Recommend 관련
    _RECOMMEND_NOT_FOUND(HttpStatus.NOT_FOUND, "RECOMMEND_001", "존재하지 않는 추천 조합입니다."),

    //CombinationImage 관련
    _COMBINATION_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMBINATION_IMAGE_001", "존재하지 않는 이미지입니다."),

    //S3 관련
    _S3_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_001", "S3에 존재하지 않는 이미지입니다"),

    //인증 관련
    _EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AUTH_001", "JWT가 존재하지 않습니다."),
    _INVALID_JWT(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 JWT입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}