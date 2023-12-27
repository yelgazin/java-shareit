package ru.practicum.shareit.booking;

public enum StateFilter {
    ALL, CURRENT, PAST, FUTURE, REJECTED;

    public static StateFilter parse(String value) {
        for (StateFilter filter : StateFilter.values()) {
            if (filter.name().equals(value)) {
                return filter;
            }
        }

        throw new UnsupportedStatusException(value);

        //throw new IllegalStateException("Unknown state: " + value);
    }
}