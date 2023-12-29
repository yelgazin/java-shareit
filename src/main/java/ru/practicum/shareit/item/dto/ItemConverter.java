package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.common.dto.AbstractEntityConverter;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;

/**
 * Конвертер для {@link Item}
 */
@Mapper(componentModel = "spring")
public interface ItemConverter extends AbstractEntityConverter<Item, ItemResponse, ItemCreateRequest,
        ItemUpdateRequest> {

    @Override
    @Mapping(source = "nextBooking.booker.id", target = "nextBooking.bookerId")
    @Mapping(source = "lastBooking.booker.id", target = "lastBooking.bookerId")
    ItemResponse convert(Item entity);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponse convert(Comment entity);
}
