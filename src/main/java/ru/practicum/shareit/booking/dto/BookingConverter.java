package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;

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

//    @Mapping(source = "booker.id", target = "bookerId")
//    ItemResponse.BookingView convert(Booking booking);
    // Booking get all for user 1
    // booker id is null.
}
