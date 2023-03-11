package com.avengers.gamera.exception;

import com.avengers.gamera.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler  {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(), List.of(e.getMessage()));
    }

    @ExceptionHandler(value = {ResourceExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleResourceNotFoundException(ResourceExistException e) {
        return new ErrorDto(HttpStatus.CONFLICT.getReasonPhrase(), List.of(e.getMessage()));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handleAccessDeniedException(AccessDeniedException e) {
        return new ErrorDto(HttpStatus.FORBIDDEN.getReasonPhrase(), List.of(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleUnexpectedExceptions(Exception e) {
        log.error("There is an unexpected exception occurred", e);
        return new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), List.of(e.getMessage()));
    }
}
