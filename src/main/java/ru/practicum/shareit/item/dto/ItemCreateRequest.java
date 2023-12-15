package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Параметры запроса для создания {@link Item}.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для создания вещи")
public class ItemCreateRequest {
    @NotBlank(message = "Наименование не может быть пустым.")
    @Schema(description = "Наименование вещи", example = "Ручная дрель")
    String name;

    @NotBlank(message = "Описание не может быть пустым.")
    @Schema(description = "Описание вещий", example = "Ручная дрель со сменным аккумулятором")
    String description;

    @NotNull(message = "Признак доступности вещи не может быть пустым.")
    @Schema(description = "Доступность", example = "true")
    Boolean available;
}
