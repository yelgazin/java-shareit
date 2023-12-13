package ru.practicum.shareit.item;

import ru.practicum.shareit.common.Repository;

import java.util.List;

public interface ItemRepository extends Repository<Item> {

    List<Item> getByUserId(long id);

    List<Item> findAvailableBySubstring(String text);
}
