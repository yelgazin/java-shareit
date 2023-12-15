package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    List<Item> getByUserId(long id);

    Item getById(long id);

    Item create(long userId, Item item);

    Item update(long userId, long itemId, Item item);

    List<Item> findAvailableBySubstring(String text);
}
