package ru.practicum.shareit.server.booking.entity;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.server.booking.exception.UnsupportedStatusException;

public enum StateFilter {

    ALL, CURRENT, PAST, FUTURE, WAITING, APPROVED, REJECTED;

    public static StateFilter parse(String value) {
        try {
            return StateFilter.valueOf(value);
        } catch (Exception ex) {
            throw new UnsupportedStatusException("Unknown state: " + value);
        }
    }

    public static class StringToStateFilterConverter implements Converter<String, StateFilter> {

        @Override
        public StateFilter convert(String source) {
            return StateFilter.parse(source);
        }
    }
}
