package ru.practicum.shareit.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Параметр запроса для создания {@link Booking}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для создания бронирования")
public class BookingCreateRequest {

    @NotNull(message = "Идентификатор вещи не может быть пустым.")
    @Schema(description = "Идентификатор вещи.")
    Long itemId;

    @NotNull
    @FutureOrPresent(message = "Дата начала бронирования должна быть больше или равна текущей дате.")
    @Schema(description = "Дата начала бронирования (включительно)", example = "2024-01-26T12:12:35")
    LocalDateTime start;

    @NotNull
    @Future(message = "Дата окончания бронирования должна быть больше текущей даты.")
    @Schema(description = "Дата окончания бронирования (включительно)", example = "2024-01-26T12:12:35")
    LocalDateTime end;
}
