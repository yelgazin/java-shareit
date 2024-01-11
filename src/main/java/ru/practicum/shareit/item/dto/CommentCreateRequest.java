package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Comment;

import javax.validation.constraints.NotBlank;

/**
 * Параметры запроса для создания {@link Comment}.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для создания комментария")
public class CommentCreateRequest {

    @NotBlank(message = "Комментарий не может быть пустым.")
    @Schema(description = "Текст комментария", example = "Отличная дрель, разбудил всех соседей")
    String text;
}
