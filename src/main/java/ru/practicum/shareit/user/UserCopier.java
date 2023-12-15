package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.common.AbstractEntityCopier;

@Mapper(componentModel = "spring")
public interface UserCopier extends AbstractEntityCopier<User> {
}
