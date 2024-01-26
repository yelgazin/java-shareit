package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Item;

import java.util.Set;

/**
 * Параметры ответа для {@link Item}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры вещи для ответа")
public class ItemResponse {

    @Schema(description = "Идентификатор пользователя", example = "1")
    Long id;

    @Schema(description = "Наименование вещи", example = "Ручная дрель")
    String name;

    @Schema(description = "Описание вещи", example = "Ручная дрель со сменным аккумулятором")
    String description;

    @Schema(description = "Доступность", example = "true")
    Boolean available;

    @Schema(description = "Идентификатор запроса вещи")
    Long requestId;

    @Schema(description = "Комментарии к вещи")
    Set<CommentResponse> comments;

    @Schema(description = "Следующее бронирование")
    BookingView nextBooking;

    @Schema(description = "Последнее бронирование")
    BookingView lastBooking;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Schema(description = "Параметры бронирования для ответа")
    public static class BookingView {

        @Schema(description = "Идентификатор бронирования")
        Long id;

        @Schema(description = "Идентификатор инициатора бронирования")
        Long bookerId;
    }
}
