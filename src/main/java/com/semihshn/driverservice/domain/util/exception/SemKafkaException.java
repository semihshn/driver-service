package com.semihshn.driverservice.domain.util.exception;

import lombok.Getter;

@Getter
public class SemKafkaException extends RuntimeException {
    private final ExceptionType exceptionType;
    private String detail;

    public SemKafkaException(ExceptionType exceptionType, String detail) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
        this.detail = detail;
    }

    public SemKafkaException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }
}
