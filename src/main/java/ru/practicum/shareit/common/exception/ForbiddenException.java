package ru.practicum.shareit.common.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
