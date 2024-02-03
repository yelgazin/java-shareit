package ru.practicum.shareit.server.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.server.common.mapper.AbstractEntityCopier;
import ru.practicum.shareit.server.item.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemCopier extends AbstractEntityCopier<Item> {
}
