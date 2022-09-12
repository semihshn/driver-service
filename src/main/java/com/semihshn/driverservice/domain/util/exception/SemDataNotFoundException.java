package com.semihshn.driverservice.domain.util.exception;

import lombok.Getter;

@Getter
public class SemDataNotFoundException extends RuntimeException {
    private final ExceptionType exceptionType;
    private String detail;

    public SemDataNotFoundException(ExceptionType exceptionType, String detail) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.detail = detail;
    }

    public SemDataNotFoundException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

}
