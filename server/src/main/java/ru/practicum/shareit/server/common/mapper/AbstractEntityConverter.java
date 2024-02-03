package ru.practicum.shareit.server.common.mapper;

import ru.practicum.shareit.server.common.entity.AbstractEntity;

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
}
