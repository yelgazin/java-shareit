package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.common.dto.AbstractEntityConverter;
import ru.practicum.shareit.user.User;

/**
 * Конвертер для {@link User}.
 */
@Mapper(componentModel = "spring")
public interface UserConverter extends AbstractEntityConverter<User, UserResponse, UserCreateRequest,
        UserUpdateRequest> {
}
