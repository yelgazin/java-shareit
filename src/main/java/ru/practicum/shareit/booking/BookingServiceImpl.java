package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingCopier bookingCopier;
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
    public List<Booking> findByBookerId(long userId, StateFilter stateFilter) {
        log.debug("Получение списка бронирований по id автора бронирования {} и фильтру {}", userId, stateFilter);

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        LocalDateTime time = LocalDateTime.now();

        switch (stateFilter) {
            case ALL: return bookingRepository.findByBookerIdOrderByStartDesc(userId);
            case CURRENT:
                return bookingRepository.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                        userId, time, time);
            case PAST: return bookingRepository.findByBookerIdAndEndLessThanOrderByStartDesc(userId, time);
            case FUTURE: return bookingRepository.findByBookerIdAndStartGreaterThanOrderByStartDesc(userId, time);
            case REJECTED: return bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(userId, Status.REJECTED);
            default: throw new NotImplementedException();
        }
    }

    @Override
    public List<Booking> findByItemOwner(long userId, StateFilter stateFilter) {
        log.debug("Получение списка бронирований по id владельца вещи {} и фильтру {}", userId, stateFilter);

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        LocalDateTime time = LocalDateTime.now();

        switch (stateFilter) {
            case ALL: return bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT:
                return bookingRepository.findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
                        userId, time, time);
            case PAST:
                return bookingRepository.findByItemOwnerIdAndEndLessThanOrderByStartDesc(userId, time);
            case FUTURE:
                return bookingRepository.findByItemOwnerIdAndStartGreaterThanOrderByStartDesc(userId, time);
            case REJECTED:
                return bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(userId, Status.REJECTED);
            default: throw new NotImplementedException();
        }
    }

    @Override
    public Booking create(long userId, Booking booking) {
        long itemId = booking.getItem().getId();
        log.debug("Создание бронирования пользователем c id {} вещи с id {}", userId, booking.getItem().getId());

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", itemId));

        if (!item.getAvailable()) {
            throw new BookingException("Вещь не доступна для бронирования.");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw  new BookingException("Бронирование своих вещей не разрешено.");
        }

        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw  new BookingException("Дата завершения бронирование должна быть позже даты начала.");
        }

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(long userId, long bookingId, Booking booking) {
//        log.debug("Обновление пользователем {} вещи {}", userId, itemId);
//        Booking savedBooking = bookingRepository.getById(bookingId)
//                .orElseThrow(() -> new EntityNotFoundException("Бронирование с id %d не найдено.", bookingId));
//        validateUpdate(userId, itemId, savedItem, item);
//        bookingCopier.update(savedItem, item);
//        return bookingRepository.update(bookingId, savedBooking);
        return null;
    }

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
            throw new ForbiddenException("Только из состояние WAITING можно менять статус.");
        }

        booking.setStatus(approve ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }

    private void validateUpdate(long userId, long itemId, Item savedItem, Item newItem) {
        log.debug("Валидация пользователем {} вещи {} при обновлении", userId, itemId);
        if (!savedItem.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может вносить изменения.");
        }
    }
}
