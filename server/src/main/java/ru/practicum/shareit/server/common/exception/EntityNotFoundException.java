package ru.practicum.shareit.server.common.exception;

public class EntityNotFoundException extends RuntimeException {

     public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
