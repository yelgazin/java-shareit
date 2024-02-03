package ru.practicum.shareit.gateway.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.booking.client.BookingClient;
import ru.practicum.shareit.server.booking.entity.StateFilter;
import ru.practicum.shareit.server.booking.dto.BookingCreateRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {

    private final BookingClient bookingClient;

    @Override
    public ResponseEntity<Object> create(long userId, BookingCreateRequest bookingCreateRequest) {
        log.debug("Создание бронирования. ИД пользователя: {}.", userId);
        return bookingClient.create(userId, bookingCreateRequest);
    }

    @Override
    public ResponseEntity<Object> approve(long userId, long bookingId, boolean approved) {
        log.debug("Утверждение / отмена бронирования. ИД пользователя: {}. ИД бронирования: {}. Подтверждение: {}.",
                userId, bookingId, approved);
        return bookingClient.approve(userId, bookingId, approved);
    }

    @Override
    public ResponseEntity<Object> getById(long userId, long bookingId) {
        log.debug("Получение бронирования. ИД пользователя: {}. ИД бронирования: {}", userId, bookingId);
        return bookingClient.getById(userId, bookingId);
    }

    @Override
    public ResponseEntity<Object> getByBookerId(long userId,
                                                StateFilter stateFilter,
                                                long from,
                                                int size) {
        log.debug("Получение своего списка бронирований. ИД пользователя: {}. Статус: {}. Записи с {} по {}.",
                userId, stateFilter, from, size);
        return bookingClient.getByBookerId(userId, stateFilter, from, size);
    }

    @Override
    public ResponseEntity<Object> getByOwner(long userId,
                                             StateFilter stateFilter,
                                             long from,
                                             int size) {
        log.debug("Получение списка бронирований веще владельца. ИД пользователя: {}. Статус: {}. Записи с {} по {}.",
                userId, stateFilter, from, size);
        return bookingClient.getByOwner(userId, stateFilter, from, size);
    }
}
