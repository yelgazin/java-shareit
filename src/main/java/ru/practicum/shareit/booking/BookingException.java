package ru.practicum.shareit.booking;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
