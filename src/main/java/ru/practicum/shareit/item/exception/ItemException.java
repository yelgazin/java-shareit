package ru.practicum.shareit.item.exception;

public class ItemException extends RuntimeException {

    public ItemException(String message) {
        super(message);
    }

    public ItemException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
