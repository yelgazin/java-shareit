package ru.practicum.shareit.common.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
