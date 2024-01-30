package ru.practicum.shareit.request.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Параметры ответа для {@link ItemRequest}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры запроса вещи для ответа")
public class ItemRequestResponse {

    @Schema(description = "Идентификатор запроса вещи", example = "1")
    Long id;

    @Schema(description = "Описание вещий в запросе", example = "Ищу ручную дрель со сменным аккумулятором")
    String description;

    @Schema(description = "Дата и время создания")
    LocalDateTime created;

    @Schema(description = "Вещи, которые были созданы по запросу")
    Set<ItemView> items;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Schema(description = "Параметры вежи для ответа")
    public static class ItemView {

        @Schema(description = "Идентификатор вещи")
        Long id;

        @Schema(description = "Наименование вещи")
        String name;

        @Schema(description = "Описание вещи")
        String description;

        @Schema(description = "Идентификатор запроса вещи")
        Long requestId;

        @Schema(description = "Доступность", example = "true")
        Boolean available;
    }
}
