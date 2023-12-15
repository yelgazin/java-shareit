package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemCopier itemCopier;
    private final UserRepository userRepository;

    @Override
    public List<Item> getByUserId(long id) {
        log.debug("Получение списка вещей пользователя с id {}", id);
        return itemRepository.getByUserId(id);
    }

    @Override
    public Item getById(long id) {
        log.debug("Получение вещи по id {}", id);
        return itemRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", id));
    }

    @Override
    public Item create(long userId, Item item) {
        log.debug("Создание пользователем {} вещи \"{}\"", userId, item.getName());
        userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        item.setOwnerId(userId);
        return itemRepository.create(item);
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        log.debug("Обновление пользователем {} вещи {}", userId, itemId);
        Item savedItem = itemRepository.getById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", itemId));
        validateUpdate(userId, itemId, savedItem, item);
        itemCopier.update(savedItem, item);
        return itemRepository.update(itemId, savedItem);
    }

    @Override
    public List<Item> findAvailableBySubstring(String text) {
        log.debug("Поиск доступных вещей по подстроке \"{}\"", text);
        return itemRepository.findAvailableBySubstring(text);
    }

    private void validateUpdate(long userId, long itemId, Item savedItem, Item newItem) {
        log.debug("Валидация пользователем {} вещи {} при обновлении", userId, itemId);
        if (!savedItem.getOwnerId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может вносить изменения.");
        }
    }
}
