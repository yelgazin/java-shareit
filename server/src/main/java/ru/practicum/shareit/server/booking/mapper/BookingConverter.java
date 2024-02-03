package ru.practicum.shareit.server.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.server.booking.dto.BookingResponse;

import java.util.List;

/**
 * Конвертер для {@link Booking}
 */
@Mapper(componentModel = "spring")
public interface BookingConverter {

    BookingResponse convert(Booking entity);

    List<BookingResponse> convert(List<Booking> entities);

    @Mapping(source = "itemId", target = "item.id")
    Booking convertCreateRequestDto(BookingCreateRequest entityCreateRequestDto);
}
