package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.common.dto.AbstractEntityConverter;
import ru.practicum.shareit.item.Comment;

/**
 * Конвертер для {@link Comment}
 */
@Mapper(componentModel = "spring")
public interface CommentConverter extends AbstractEntityConverter<Comment, CommentResponse, CommentCreateRequest,
        CommentUpdateRequest> {
}
