package ru.practicum.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final BadRequestException e) {
        log.info("400 {}", e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "Ошибка в запросе",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info("500 {}", e.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Ошибка сервера",
                e.getMessage());
    }
}

