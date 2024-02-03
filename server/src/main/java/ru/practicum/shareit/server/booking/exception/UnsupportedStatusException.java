package ru.practicum.shareit.server.booking.exception;

public class UnsupportedStatusException extends RuntimeException {

    public UnsupportedStatusException(String message) {
        super(message);
    }
}
