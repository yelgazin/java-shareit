package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Comment;

/**
 * Конвертер для {@link Comment}
 */
@Mapper(componentModel = "spring")
public interface CommentConverter {

    Comment convert(CommentCreateRequest commentCreateRequest);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponse convert(Comment entity);
}
