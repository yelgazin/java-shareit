package ru.practicum.shareit.server.common.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.server.common.entity.AbstractEntity;

/**
 * @param <T> Entity
 */
public interface AbstractEntityCopier<T extends AbstractEntity> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget T entity, T sourceEntity);
}
