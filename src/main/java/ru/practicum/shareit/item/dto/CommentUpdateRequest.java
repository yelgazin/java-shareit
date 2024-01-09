package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Comment;

/**
 * Параметр запроса для редактирования {@link Comment}.
 * Содержит только поля, которые можно редактировать.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры для редактирования комментария")
public class CommentUpdateRequest {
}
