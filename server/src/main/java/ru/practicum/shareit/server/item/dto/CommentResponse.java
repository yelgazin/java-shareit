package ru.practicum.shareit.server.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.item.entity.Comment;

import java.time.LocalDateTime;

/**
 * Параметры ответа для {@link Comment}.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Параметры вещи для комментария")
public class CommentResponse {

    @Schema(description = "Идентификатор", example = "1")
    Long id;

    @Schema(description = "Автор", example = "Петров Сергей")
    String authorName;

    @Schema(description = "Дата и время создания")
    LocalDateTime created;

    @Schema(description = "Текст комментария", example = "Отличная дрель, разбудил всех соседей")
    String text;
}
