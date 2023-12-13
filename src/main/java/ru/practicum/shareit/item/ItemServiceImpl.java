package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemCopier itemCopier;
    private final UserRepository userRepository;

    @Override
    public List<Item> getByUserId(long id) {
        return itemRepository.getByUserId(id);
    }

    @Override
    public Item getById(long id) {
        return itemRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", id));
    }

    @Override
    public Item create(long userId, Item item) {
        userRepository.getById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        item.setOwnerId(userId);
        return itemRepository.create(item);
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        Item savedItem = itemRepository.getById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с id %d не найдена.", itemId));
        validateUpdate(userId, itemId, savedItem, item);
        itemCopier.update(savedItem, item);
        return itemRepository.update(itemId, savedItem);
    }

    @Override
    public List<Item> findAvailableBySubstring(String text) {
        return itemRepository.findAvailableBySubstring(text);
    }

    private void validateUpdate(long userId, long itemId, Item savedItem, Item newItem) {
        if (!savedItem.getOwnerId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может вносить изменения.");
        }
    }
}
