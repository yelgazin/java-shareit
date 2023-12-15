package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.common.AbstractEntityCopier;

@Mapper(componentModel = "spring")
public interface ItemCopier extends AbstractEntityCopier<Item> {
}
