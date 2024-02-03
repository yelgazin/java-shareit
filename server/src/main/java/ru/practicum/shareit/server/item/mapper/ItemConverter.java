package ru.practicum.shareit.server.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.item.entity.Comment;
import ru.practicum.shareit.server.item.entity.Item;
import ru.practicum.shareit.server.item.dto.CommentResponse;
import ru.practicum.shareit.server.item.dto.ItemCreateRequest;
import ru.practicum.shareit.server.item.dto.ItemResponse;
import ru.practicum.shareit.server.item.dto.ItemUpdateRequest;

import java.util.List;

/**
 * Конвертер для {@link Item}
 */
@Mapper(componentModel = "spring")
public interface ItemConverter {

    @Mapping(source = "nextBooking",
            target = "nextBooking",
            conditionExpression = "java(entity.getOwner().getId().equals(requestUserid))")
    @Mapping(source = "lastBooking",
            target = "lastBooking",
            conditionExpression = "java(entity.getOwner().getId().equals(requestUserid))")
    ItemResponse convert(Item entity, @Context long requestUserid);

    @Mapping(source = "booker.id", target = "bookerId")
    ItemResponse.BookingView convert(Booking booking);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponse convert(Comment entity);

    List<ItemResponse> convert(List<Item> entities, @Context long requestUserId);

    Item convertCreateRequestDto(ItemCreateRequest entityCreateRequestDto);

    Item convertUpdateRequestDto(ItemUpdateRequest entityUpdateRequestDto);
}
