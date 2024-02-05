package ru.practicum.shareit.server.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.server.common.mapper.AbstractEntityConverter;
import ru.practicum.shareit.server.user.dto.UserCreateRequest;
import ru.practicum.shareit.server.user.dto.UserResponse;
import ru.practicum.shareit.server.user.dto.UserUpdateRequest;
import ru.practicum.shareit.server.user.entity.User;

/**
 * Конвертер для {@link User}.
 */
@Mapper(componentModel = "spring")
public interface UserConverter extends AbstractEntityConverter<User, UserResponse, UserCreateRequest,
        UserUpdateRequest> {
}
