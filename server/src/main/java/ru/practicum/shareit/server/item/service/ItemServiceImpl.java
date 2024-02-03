package ru.practicum.shareit.server.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.entity.Status;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.common.exception.EntityNotFoundException;
import ru.practicum.shareit.server.common.exception.ForbiddenException;
import ru.practicum.shareit.server.item.entity.Comment;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.exception.ItemException;
import ru.practicum.shareit.server.item.mapper.ItemCopier;
import ru.practicum.shareit.server.item.repository.CommentRepository;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.entity.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemCopier itemCopier;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Item> getByUserId(long userId, long from, int size) {
        log.debug("Получение списка вещей пользователя с id {}", userId);
        PageRequest pageRequest = PageRequest.of((int) (from / size), size);
        List<Item> items = itemRepository.getByOwnerIdOrderByIdAsc(userId, pageRequest).getContent();

        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        Map<Long, Booking> nextBookingMap = bookingRepository
                .findNextBookings(items.stream().map(Item::getId).collect(Collectors.toList()),
                        Status.APPROVED.name(),
                        currentLocalDateTime)
                .stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), booking -> booking));

        Map<Long, Booking> lastBookingMap = bookingRepository
                .findLastBookings(items.stream().map(Item::getId).collect(Collectors.toList()),
                        Status.APPROVED.name(),
                        currentLocalDateTime)
                .stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), booking -> booking));

        items.forEach(item -> {
            item.setLastBooking(lastBookingMap.get(item.getId()));
            item.setNextBooking(nextBookingMap.get(item.getId()));
        });

        return items;
    }

    @Override
    public Item getById(long userId, long id) {
        log.debug("Получение вещи по id {}", id);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", id));

        if (item.getOwner().getId().equals(userId)) {

            LocalDateTime currentLocalDateTime = LocalDateTime.now();

            bookingRepository.findNextBookings(List.of(item.getId()), Status.APPROVED.name(), currentLocalDateTime)
                    .stream().findAny().ifPresent(item::setNextBooking);

            bookingRepository.findLastBookings(List.of(item.getId()), Status.APPROVED.name(), currentLocalDateTime)
                    .stream().findAny().ifPresent(item::setLastBooking);
        }

        return item;
    }

    @Transactional
    @Override
    public Item create(long userId, Item item) {
        log.debug("Создание пользователем {} вещи \"{}\"", userId, item.getName());
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item update(long userId, long itemId, Item item) {
        log.debug("Обновление пользователем {} вещи {}", userId, itemId);
        Item savedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", itemId));
        validateUpdate(userId, itemId, savedItem, item);
        itemCopier.update(savedItem, item);
        return itemRepository.save(savedItem);
    }

    @Override
    public List<Item> findAvailableBySubstring(String text, long from, int size) {
        log.debug("Поиск доступных вещей по подстроке \"{}\"", text);
        PageRequest pageRequest = PageRequest.of((int) (from / size), size);
        return itemRepository.findAvailableBySubstring(text, pageRequest).getContent();
    }

    @Transactional
    @Override
    public Comment addComment(long userId, long itemId, Comment comment) {
        log.debug("Добавление комментария к вещи с id {}", itemId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", itemId));

        LocalDateTime time = LocalDateTime.now();

        bookingRepository.findByBookerIdAndStartLessThanAndStatusIsOrderByStartDesc(userId, time, Status.APPROVED)
                .stream()
                .filter(booking -> booking.getItem().getId().equals(itemId))
                .findAny()
                .orElseThrow(() -> new ItemException("Оставить отзыв можно только по арендованным вещам."));

        comment.setAuthor(user);
        comment.setItem(item);
        commentRepository.save(comment);

        return comment;
    }

    private void validateUpdate(long userId, long itemId, Item savedItem, Item newItem) {
        log.debug("Валидация пользователем {} вещи {} при обновлении", userId, itemId);
        if (!savedItem.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может вносить изменения.");
        }
    }
}
