package ru.practicum.shareit.server.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.item.entity.Item;

/**
 * Параметр запроса для редактирования {@link Item}.
 * Содержит только поля, которые можно редактировать.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для редактирования вещи")
public class ItemUpdateRequest {

    @Schema(description = "Наименование вещи", example = "Ручная дрель")
    String name;

    @Schema(description = "Описание вещий", example = "Ручная дрель со сменным аккумулятором")
    String description;

    @Schema(description = "Доступность", example = "true")
    Boolean available;
}
