package ru.practicum.shareit.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingException;
import ru.practicum.shareit.booking.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.exception.ItemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public RestException handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Неизвестная ошибка валидации.");
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public RestException handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    public RestException handleEntityAlreadyExistsException(Exception ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public RestException handleNotFoundException(Exception ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ForbiddenException.class})
    public RestException handleForbiddenException(Exception ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BookingException.class})
    public RestException handleBookingException(Exception ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ItemException.class})
    public RestException handleItemException(ItemException ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UnsupportedStatusException.class)
    public RestException handleUnsupportedStateException(UnsupportedStatusException ex) {
        String message = ex.getMessage();
        log.info(message);
        return new RestException(message, ex.getCause(), HttpStatus.BAD_REQUEST);
    }
}