package ru.practicum.shareit.server.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.server.common.mapper.AbstractEntityCopier;
import ru.practicum.shareit.server.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserCopier extends AbstractEntityCopier<User> {
}
