package ru.practicum.shareit.server.booking.service;

import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.entity.StateFilter;

import java.util.List;

public interface BookingService {

    Booking getById(long userId, long id);

    List<Booking> findByBookerId(long userId, StateFilter stateFilter, long from, int size);

    List<Booking> findByItemOwner(long userId, StateFilter stateFilter, long from, int size);

    Booking create(long userId, Booking booking);

    Booking setApproved(long userId, long bookingId, boolean approve);
}
