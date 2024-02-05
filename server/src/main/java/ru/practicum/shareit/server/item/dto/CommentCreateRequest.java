package ru.practicum.shareit.server.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.item.entity.Comment;

import javax.validation.constraints.NotBlank;

/**
 * Параметры запроса для создания {@link Comment}.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для создания комментария")
public class CommentCreateRequest {

    @NotBlank(message = "Комментарий не может быть пустым.")
    @Schema(description = "Текст комментария", example = "Отличная дрель, разбудил всех соседей")
    String text;
}
