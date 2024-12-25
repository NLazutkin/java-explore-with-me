package ru.practicum.ewm.base.exceptions.ErrorBase;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.base.exceptions.*;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.error(HttpStatus.CONFLICT.getReasonPhrase(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "Нарушено ограничение целостности",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConditionsNotMetException e) {
        log.error(HttpStatus.CONFLICT.getReasonPhrase(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "Не соблюдаются условия для вызываемой операции",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final DuplicatedDataException e) {
        log.error(HttpStatus.CONFLICT.getReasonPhrase(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "Дублирование ключевого значения",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error(HttpStatus.NOT_FOUND.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.toString(),
                "Объект запроса не найден",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.error(HttpStatus.BAD_REQUEST.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Объект запроса не найден",
                e.getMessage());
    }


    // Для валидации через jakarta.validation.constraints
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(HttpStatus.BAD_REQUEST.toString(), e.getLocalizedMessage(), e.getMessage());
        String field = Objects.requireNonNull(e.getFieldError()).getField();

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Неправильно составлен запрос",
                String.format("Поле: %s. Некорректное значения поля. Значение: %s", field, e.getFieldValue(field)));
    }

    // Для валидации через jakarta.validation.constraints
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(HttpStatus.BAD_REQUEST.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Неправильно составлен запрос",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Ошибка сервера",
                e.getMessage());
    }
}

