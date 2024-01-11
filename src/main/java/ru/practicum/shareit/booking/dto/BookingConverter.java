package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.common.dto.AbstractEntityConverter;

/**
 * Конвертер для {@link Booking}
 */
@Mapper(componentModel = "spring")
public interface BookingConverter extends AbstractEntityConverter<Booking, BookingResponse, BookingCreateRequest,
        BookingUpdateRequest> {

    @Override
    @Mapping(source = "itemId", target = "item.id")
    Booking convertCreateRequestDto(BookingCreateRequest entityCreateRequestDto);
}
