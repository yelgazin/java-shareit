package ru.practicum.shareit.common;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @param <T> Entity
 */
public interface AbstractEntityCopier<T extends AbstractEntity> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget T entity, T sourceEntity);
}
