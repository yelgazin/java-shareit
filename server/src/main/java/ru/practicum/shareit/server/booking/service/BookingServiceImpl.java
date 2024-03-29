package ru.practicum.shareit.server.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.booking.entity.StateFilter;
import ru.practicum.shareit.server.booking.entity.Status;
import ru.practicum.shareit.server.booking.exception.BookingException;
import ru.practicum.shareit.server.common.exception.EntityNotFoundException;
import ru.practicum.shareit.server.common.exception.ForbiddenException;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking getById(long userId, long id) {
        log.debug("Получение бронирования по id {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование с id %d не найдено.", id));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи или автор бронирования может получать сведения " +
                    "о бронировании.");
        }

        return booking;
    }

    @Override
    public List<Booking> findByBookerId(long userId, StateFilter stateFilter, long from, int size) {
        log.debug("Получение списка бронирований по id автора бронирования {} и фильтру {}", userId, stateFilter);

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        LocalDateTime time = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of((int) (from / size), size);

        switch (stateFilter) {
            case ALL:
                return bookingRepository.findByBookerIdOrderByStartDesc(userId, pageRequest).getContent();
            case CURRENT:
                return bookingRepository
                        .findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                                userId, time, time, pageRequest)
                        .getContent();
            case PAST:
                return bookingRepository
                        .findByBookerIdAndEndLessThanOrderByStartDesc(userId, time, pageRequest)
                        .getContent();
            case FUTURE:
                return bookingRepository
                        .findByBookerIdAndStartGreaterThanOrderByStartDesc(userId, time, pageRequest)
                        .getContent();
            case REJECTED:
                return bookingRepository
                        .findByBookerIdAndStatusIsOrderByStartDesc(userId, Status.REJECTED, pageRequest)
                        .getContent();
            case APPROVED:
                return bookingRepository
                        .findByBookerIdAndStatusIsOrderByStartDesc(userId, Status.APPROVED, pageRequest)
                        .getContent();
            case WAITING:
                return bookingRepository
                        .findByBookerIdAndStatusIsOrderByStartDesc(userId, Status.WAITING, pageRequest)
                        .getContent();
            default:
                throw new NotImplementedException();
        }
    }

    @Override
    public List<Booking> findByItemOwner(long userId, StateFilter stateFilter, long from, int size) {
        log.debug("Получение списка бронирований по id владельца вещи {} и фильтру {}", userId, stateFilter);

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        LocalDateTime time = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of((int) (from / size), size);

        switch (stateFilter) {
            case ALL:
                return bookingRepository
                        .findByItemOwnerIdOrderByStartDesc(userId, pageRequest).getContent();
            case CURRENT:
                return bookingRepository
                        .findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                                userId, time, time, pageRequest).getContent();
            case PAST:
                return bookingRepository
                        .findByItemOwnerIdAndEndLessThanOrderByStartDesc(userId, time, pageRequest)
                        .getContent();
            case FUTURE:
                return bookingRepository
                        .findByItemOwnerIdAndStartGreaterThanOrderByStartDesc(userId, time, pageRequest)
                        .getContent();
            case REJECTED:
                return bookingRepository
                        .findByItemOwnerIdAndStatusIsOrderByStartDesc(userId, Status.REJECTED, pageRequest)
                        .getContent();
            case APPROVED:
                return bookingRepository
                        .findByItemOwnerIdAndStatusIsOrderByStartDesc(userId, Status.APPROVED, pageRequest)
                        .getContent();
            case WAITING:
                return bookingRepository
                        .findByItemOwnerIdAndStatusIsOrderByStartDesc(userId, Status.WAITING, pageRequest)
                        .getContent();
            default:
                throw new NotImplementedException();
        }
    }

    @Transactional
    @Override
    public Booking create(long userId, Booking booking) {
        long itemId = booking.getItem().getId();
        log.debug("Создание бронирования пользователем c id {} вещи с id {}", userId, booking.getItem().getId());

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", itemId));

        if (!item.getAvailable()) {
            throw new BookingException("Вещь %d не доступна для бронирования.", itemId);
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Бронирование своих вещей не разрешено.");
        }

        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw new BookingException("Дата завершения бронирование должна быть позже даты начала.");
        }

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking setApproved(long userId, long bookingId, boolean approve) {
        log.debug("Обновление статуса бронирования с id {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование с id %d не найдено.", bookingId));

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владельцам вещи разрешено изменять статус бронирования.");
        }

        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new BookingException("Только из состояние WAITING можно менять статус.");
        }

        booking.setStatus(approve ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }
}
