package ru.practicum.shareit.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public RestException handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream().findFirst().get().getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public RestException handleNotFoundException(Exception ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {EntityAlreadyExistsException.class})
    public RestException handleEntityAlreadyExistsException(Exception ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {ForbiddenException.class})
    public RestException handleForbiddenException(Exception ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestException handleRuntimeException(final RuntimeException exception) {
        log.error("Внутренняя ошибка сервера. {}", exception.getMessage());
        return new RestException("Произошла непредвиденная ошибка.", exception.getCause(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}