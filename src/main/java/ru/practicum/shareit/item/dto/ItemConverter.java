package ru.practicum.shareit.item.dto;

import org.mapstruct.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;

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
