package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.common.dto.AbstractEntityConverter;

/**
 * Конвертер для {@link Booking}
 */
@Mapper(componentModel = "spring")
public interface BookingConverter extends AbstractEntityConverter<Booking, BookingResponse, BookingCreateRequest,
        BookingUpdateRequest> {
}
