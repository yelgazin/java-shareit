package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Comment;
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

    @Schema(description = "Описание вещий", example = "Ручная дрель со сменным аккумулятором")
    String description;

    @Schema(description = "Доступность", example = "true")
    Boolean available;

    @Schema(description = "Комментарии к вещи")
    Set<Comment> comments;
}
