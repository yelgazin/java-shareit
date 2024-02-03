package ru.practicum.shareit.server.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.booking.entity.Booking;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Параметр запроса для создания {@link Booking}.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для создания бронирования")
public class BookingCreateRequest {

    @NotNull(message = "Идентификатор вещи не может быть пустым.")
    @Schema(description = "Идентификатор вещи.")
    Long itemId;

    @NotNull(message = "Дата начала бронирования не может быть пустой.")
    @FutureOrPresent(message = "Дата начала бронирования должна быть больше или равна текущей даты.")
    @Schema(description = "Дата начала бронирования (включительно)", example = "2024-01-26T12:12:35")
    LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования не может быть пустой.")
    @Future(message = "Дата окончания бронирования должна быть больше текущей даты.")
    @Schema(description = "Дата окончания бронирования (включительно)", example = "2024-01-26T12:12:35")
    LocalDateTime end;
}
