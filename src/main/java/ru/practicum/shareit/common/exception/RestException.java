package ru.practicum.shareit.common.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestException {
    final String message;
    final Throwable throwable;
    final HttpStatus httpStatus;
}