package ru.practicum.shareit.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;

/**
 * Параметр запроса для создания {@link Booking}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для создания бронирования")
public class BookingCreateRequest {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
