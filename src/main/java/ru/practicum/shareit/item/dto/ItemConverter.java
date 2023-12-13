package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.common.dto.AbstractEntityConverter;
import ru.practicum.shareit.item.Item;

/**
 * Конвертер для {@link Item}
 */
@Mapper(componentModel = "spring")
public interface ItemConverter extends AbstractEntityConverter<Item, ItemResponse, ItemCreateRequest,
        ItemUpdateRequest> {
}
