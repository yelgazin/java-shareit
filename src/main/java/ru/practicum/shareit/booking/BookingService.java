package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking getById(long userId, long id);

    List<Booking> findByBookerId(long userId, StateFilter stateFilter);

    List<Booking> findByItemOwner(long userId, StateFilter stateFilter);

    Booking create(long userId, Booking booking);

    Booking setApproved(long userId, long bookingId, boolean approve);
}
