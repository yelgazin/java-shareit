package ru.practicum.shareit.common.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.common.AbstractEntity;

import java.util.List;

/**
 * @param <T> Entity
 * @param <S> DTO response
 * @param <U> DTO create request
 * @param <V> DTO update request
 */
public interface AbstractEntityConverter<T extends AbstractEntity, S, U, V> {

    S convert(T entity);

    List<S> convert(List<T> entities);

    T convertCreateRequestDto(U entityCreateRequestDto);

    T convertUpdateRequestDto(V entityUpdateRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget T entity, T updateEntity);
}
