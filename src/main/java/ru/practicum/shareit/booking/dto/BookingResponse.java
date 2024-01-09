package ru.practicum.shareit.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.user.dto.UserResponse;

import java.time.LocalDateTime;

/**
 * Параметры ответа для {@link Booking}.
 */
@Getter
@Setter
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
