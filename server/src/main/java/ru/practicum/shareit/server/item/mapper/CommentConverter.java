package ru.practicum.shareit.server.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.server.item.entity.Comment;
import ru.practicum.shareit.server.item.dto.CommentCreateRequest;
import ru.practicum.shareit.server.item.dto.CommentResponse;

/**
 * Конвертер для {@link Comment}
 */
@Mapper(componentModel = "spring")
public interface CommentConverter {

    Comment convert(CommentCreateRequest commentCreateRequest);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponse convert(Comment entity);
}
