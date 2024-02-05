package ru.practicum.shareit.server.item.service;

import ru.practicum.shareit.server.item.entity.Comment;
import ru.practicum.shareit.server.item.entity.Item;

import java.util.List;

public interface ItemService {

    List<Item> getByUserId(long id, long from, int size);

    Item getById(long userId, long id);

    Item create(long userId, Item item);

    Item update(long userId, long itemId, Item item);

    List<Item> findAvailableBySubstring(String text, long from, int size);

    Comment addComment(long userId, long itemId, Comment comment);
}
