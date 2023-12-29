package ru.practicum.shareit.booking.exception;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
