package com.example.dgbackend.global.exception;

import com.example.dgbackend.global.common.response.code.ErrorReasonDto;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public ApiException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        String errorCode = errorStatus.getCode();
        String errorMessage = errorStatus.getMessage();
        log.error("In ApiException ErrorCode: {}, ErrorMessage: {}, Status: {}", errorCode,
            errorMessage, errorStatus);
        this.errorStatus = errorStatus;
    }

    public ErrorReasonDto getErrorReason() {
        return this.errorStatus.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.errorStatus.getReasonHttpStatus();
    }
}