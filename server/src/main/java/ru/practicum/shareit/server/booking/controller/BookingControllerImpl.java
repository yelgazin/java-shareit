package ru.practicum.shareit.server.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.entity.StateFilter;
import ru.practicum.shareit.server.booking.mapper.BookingConverter;
import ru.practicum.shareit.server.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.server.booking.dto.BookingResponse;
import ru.practicum.shareit.server.booking.service.BookingService;

import java.util.List;

/**
 * Имплементация контроллера для {@link Booking}.
 */
@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {

    private final BookingConverter bookingConverter;
    private final BookingService bookingService;

    /**
     * {@inheritDoc}
     */
    @Override
    public BookingResponse create(long userId, BookingCreateRequest bookingCreateRequest) {
        return bookingConverter.convert(
                bookingService.create(userId, bookingConverter.convertCreateRequestDto(bookingCreateRequest)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookingResponse approve(long userId, long bookingId, boolean approved) {
        return bookingConverter.convert(bookingService.setApproved(userId, bookingId, approved));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookingResponse getById(long userId, long bookingId) {
        return bookingConverter.convert(bookingService.getById(userId, bookingId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingResponse> getByBookerId(long userId, StateFilter stateFilter, long from, int size) {
        return bookingConverter.convert(bookingService.findByBookerId(userId, stateFilter, from, size));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookingResponse> getByOwner(long userId, StateFilter stateFilter, long from, int size) {
        return bookingConverter.convert(bookingService.findByItemOwner(userId, stateFilter, from, size));
    }
}
