package com.example.dgbackend.global.exception;

import com.example.dgbackend.global.common.response.code.ErrorReasonDto;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;


public class ApiException extends RuntimeException{

    private final ErrorStatus errorStatus;

    public ApiException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorReasonDto getErrorReason() {
        return this.errorStatus.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.errorStatus.getReasonHttpStatus();
    }
}