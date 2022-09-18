package com.semihshn.driverservice.adapter.rest.common;

import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemAuthenticationException;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import com.semihshn.driverservice.domain.util.exception.SemKafkaException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SemExceptionHandler {

    @ExceptionHandler(SemDataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleDataNotFoundException(SemDataNotFoundException e) {
        return new ExceptionResponse(e.getExceptionType(), e.getDetail());
    }

    @ExceptionHandler(SemAuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAuthenticationException(SemAuthenticationException e) {
        return new ExceptionResponse(e.getExceptionType(), e.getDetail());
    }

    @ExceptionHandler(SemKafkaException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleKafkaException(SemKafkaException e) {
        return new ExceptionResponse(e.getExceptionType(), e.getDetail());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception e) {
        return new ExceptionResponse(ExceptionType.GENERIC_EXCEPTION, e.getMessage());
    }
}
