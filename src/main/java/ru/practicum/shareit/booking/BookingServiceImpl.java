package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingCopier bookingCopier;
    private final UserRepository userRepository;

    @Override
    public List<Booking> getByUserId(long id) {
        log.debug("Получение списка вещей пользователя с id {}", id);
        return null; //bookingRepository.getByUserId(id);
    }

    @Override
    public Booking getById(long userId, long id) {
        log.debug("Получение вещи по id {}", id);
        return bookingRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", id));
    }

    @Override
    public Booking create(long userId, Booking booking) {
//        log.debug("Создание пользователем {} вещи \"{}\"", userId, booking.getName());
//        userRepository.getById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));
//
//        booking.setOwnerId(userId);
        return bookingRepository.create(booking);
    }

    @Override
    public Booking update(long userId, long bookingId, Booking booking) {
//        log.debug("Обновление пользователем {} вещи {}", userId, itemId);
        Booking savedBooking = bookingRepository.getById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование с id %d не найдено.", bookingId));
//        validateUpdate(userId, itemId, savedItem, item);
//        bookingCopier.update(savedItem, item);
        return bookingRepository.update(bookingId, savedBooking);
    }

//    @Override
//    public List<Item> findAvailableBySubstring(String text) {
//        log.debug("Поиск доступных вещей по подстроке \"{}\"", text);
//        return bookingRepository.findAvailableBySubstring(text);
//    }

    @Override
    public Booking setApproved(long userId, long bookingId, boolean approve) {
        return null;
    }

    @Override
    public List<Booking> findByStateFilter(long userId, StateFilter stateFilter) {
        return null;
    }

    private void validateUpdate(long userId, long itemId, Item savedItem, Item newItem) {
        log.debug("Валидация пользователем {} вещи {} при обновлении", userId, itemId);
        if (!savedItem.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может вносить изменения.");
        }
    }
}
