package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    List<Item> getByUserId(long id, long from, int size);

    Item getById(long id);

    Item create(long userId, Item item);

    Item update(long userId, long itemId, Item item);

    List<Item> findAvailableBySubstring(String text, long from, int size);

    Comment addComment(long userId, long itemId, Comment comment);
}
