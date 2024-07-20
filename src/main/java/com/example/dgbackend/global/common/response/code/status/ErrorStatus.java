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
    _FORBIDDEN_MEMBER_REQUEST(HttpStatus.FORBIDDEN, "MEMBER_002", "해당 사용자는 금지된 요청을 하였습니다."),
    _PERMANENTLY_REPORTED_MEMBER(HttpStatus.FORBIDDEN, "MEMBER_003", "해당 사용자는 영구 정지되었습니다."),
    _TEMPORARILY_REPORTED_MEMBER(HttpStatus.FORBIDDEN, "MEMBER_004", "해당 사용자는 일시 정지되었습니다."),
    _DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER_005", "해당 닉네임은 중복된 닉네임입니다."),
    _INVALID_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER_006", "유효하지 않은 사용자입니다."),

    //오늘의 조합 관련
    _COMBINATION_NOT_FOUND(HttpStatus.NOT_FOUND, "COMBINATION_001", "존재하지 않는 오늘의 조합입니다."),
    _DELETE_COMBINATION(HttpStatus.BAD_REQUEST, "COMBINATION_002", "삭제된 오늘의 조합입니다."),

    //오늘의 조합 댓글
    _COMBINATION_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMBINATION_COMMENT_001", "존재하지 않는 오늘의 조합 댓글입니다."),
    _OVER_DEPTH_COMBINATION_COMMENT(HttpStatus.BAD_REQUEST, "COMBINATION_COMMENT_002", "대댓글까지만 가능합니다."),
    _DELETE_COMBINATION_COMMENT(HttpStatus.BAD_REQUEST, "COMBINATION_COMMENT_003", "이미 삭제된 오늘의 조합 댓글입니다."),

    //CombinationImage 관련
    _COMBINATION_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMBINATION_IMAGE_001", "존재하지 않는 이미지입니다."),

    //S3 관련
    _S3_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_001", "S3에 존재하지 않는 이미지입니다"),

    //Paging 관련
    _PAGE_RANGE_ERROR(HttpStatus.BAD_REQUEST, "PAGE_001", "적절한 페이지 번호가 아닙니다."),

    //인증 관련
    _EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AUTH_001", "JWT가 존재하지 않습니다."),
    _INVALID_JWT(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 JWT입니다."),
    _REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_003", "Refresh Token이 존재하지 않습니다."),
    _EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "만료된 Token입니다."),

    //Redis 관련
    _REDIS_NOT_FOUND(HttpStatus.NOT_FOUND, "REDIS_001", "Redis에 존재하지 않는 Key 입니다."),

    //Recommend 관련
    _RECOMMEND_NOT_FOUND(HttpStatus.NOT_FOUND, "RECOMMEND_001", "존재하지 않는 추천 조합입니다."),
    _NULL_DESIRE_LEVEL(HttpStatus.BAD_REQUEST, "RECOMMEND_002", "취하고 싶은 정도를 입력해주세요."),
    _NULL_FOOD_NAME(HttpStatus.BAD_REQUEST, "RECOMMEND_003", "음식 이름을 입력해주세요."),
  
    //레시피
    _EMPTY_RECIPE(HttpStatus.CONFLICT, "RECIPE_001", "존재하지 않는 레시피입니다."),
    _DELETE_RECIPE(HttpStatus.BAD_REQUEST, "RECIPE_002", "삭제된 레시피입니다."),
    _ALREADY_CREATE_RECIPE(HttpStatus.BAD_REQUEST, "RECIPE_003", "이미 존재하는 레시피입니다."),

    //레시피 댓글
    _EMPTY_RECIPE_COMMENT(HttpStatus.CONFLICT, "RECIPE_COMMENT_001", "존재하지 않는 레시피 댓글입니다."),
    _OVER_DEPTH_RECIPE_COMMENT(HttpStatus.BAD_REQUEST, "RECIPE_COMMENT_003", "대댓글까지만 가능합니다."),
    _Already_DELETE_RECIPE_COMMENT(HttpStatus.BAD_REQUEST, "RECIPE_COMMENT_004", "이미 삭제된 댓글입니다."),

    //레시피 이미지
    _EMPTY_RECIPE_IMAGE(HttpStatus.CONFLICT, "RECIPE_IMAGE_001", "존재하지 않는 레시피 이미지입니다."),
    _NOTHING_RECIPE_IMAGE(HttpStatus.BAD_REQUEST, "RECIPE_IMAGE_001", "레시피 이미지가 없습니다."),

    //새로 출시된 주류
    _EMPTY_DRINK_LIST(HttpStatus.CONFLICT, "DRINK_001", "존재하지 않는 주류입니다."),

    // 신고
    _DUPLICATE_REPORT(HttpStatus.CONFLICT, "REPORT_001", "이미 존재하는 신고 내용입니다."),
    _FAIL_SEND_MAIL(HttpStatus.INTERNAL_SERVER_ERROR, "REPORT_002", "메일 전송에 실패했습니다."),

    //차단
    _DUPLICATE_MEMBER_BLOCK(HttpStatus.CONFLICT,"MEMBER_BLOCK_001", "이미 차단된 멤버입니다."),
    _BLOCKED_MEMBER(HttpStatus.CONFLICT,"MEMBER_BLOCK_002", "차단된 멤버입니다."),
    _INVALID_MEMBER_BLOCK(HttpStatus.BAD_REQUEST,"MEMBER_BLOCK_002","본인 차단은 불가능합니다.");

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
                .build();
    }
}