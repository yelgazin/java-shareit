package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemCopier itemCopier;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Item> getByUserId(long id) {
        log.debug("Получение списка вещей пользователя с id {}", id);
        return itemRepository.getByOwnerId(id);
    }

    @Override
    public Item getById(long id) {
        log.debug("Получение вещи по id {}", id);
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", id));
    }

    @Override
    public Item create(long userId, Item item) {
        log.debug("Создание пользователем {} вещи \"{}\"", userId, item.getName());
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        item.setOwner(owner);
        return itemRepository.save(item);
    }

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
    public List<Item> findAvailableBySubstring(String text) {
        log.debug("Поиск доступных вещей по подстроке \"{}\"", text);
        return itemRepository.findAvailableBySubstring(text);
    }

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
                .orElseThrow(() -> new ForbiddenException("Оставить отзыв можно только по арендованным вещам."));

        comment.setAuthor(user);
        //comment.setItem(item);
        item.getComments().add(comment);

        commentRepository.save(comment);
        itemRepository.save(item);

        return comment;
    }

    private void validateUpdate(long userId, long itemId, Item savedItem, Item newItem) {
        log.debug("Валидация пользователем {} вещи {} при обновлении", userId, itemId);
        if (!savedItem.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может вносить изменения.");
        }
    }
}
