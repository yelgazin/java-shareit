package ru.practicum.shareit.server.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.booking.entity.Booking;
import ru.practicum.shareit.server.booking.entity.Status;
import ru.practicum.shareit.server.item.dto.ItemResponse;
import ru.practicum.shareit.server.user.dto.UserResponse;

import java.time.LocalDateTime;

/**
 * Параметры ответа для {@link Booking}.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры бронирования для ответа")
public class BookingResponse {

    @Schema(description = "Идентификатор бронирования", example = "1")
    Long id;

    @Schema(description = "Вещь")
    ItemResponse item;

    @Schema(description = "Арендатор")
    UserResponse booker;

    @Schema(description = "Статус бронирования")
    Status status;

    @Schema(description = "Дата начала бронирования (включительно)")
    LocalDateTime start;

    @Schema(description = "Дата окончания бронирования (включительно)")
    LocalDateTime end;
}
