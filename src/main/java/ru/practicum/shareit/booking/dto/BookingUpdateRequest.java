package ru.practicum.shareit.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;

/**
 * Параметр запроса для редактирования {@link Booking}.
 * Содержит только поля, которые можно редактировать.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для редактирования бронирования ")
public class BookingUpdateRequest {
    @Schema(description = "Доступность", example = "true")
    Boolean available;
}
