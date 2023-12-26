package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    List<Booking> getByUserId(long id);

    Booking getById(long userId, long id);

    Booking create(long userId, Booking booking);

    Booking update(long userId, long bookingId, Booking booking);

    //   List<Booking> findAvailableBySubstring(String text);

    Booking setApproved(long userId, long bookingId, boolean approve);

    List<Booking> findByStateFilter(long userId, StateFilter stateFilter);
}
