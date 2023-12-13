package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCreateRequest {
    @NotBlank(message = "Наименование не может быть пустым.")
    String name;
    @NotBlank(message = "Описание не может быть пустым.")
    String description;
    @NotNull(message = "Признак доступности вещи не может быть пустым.")
    Boolean available;
}
