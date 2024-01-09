package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.exception.UnsupportedStatusException;

public enum StateFilter {

    ALL, CURRENT, PAST, FUTURE, WAITING, APPROVED, REJECTED;

    public static StateFilter parse(String value) {
        try {
            return StateFilter.valueOf(value);
        } catch (Exception ex) {
            throw new UnsupportedStatusException("Unknown state: " + value);
        }
    }
}