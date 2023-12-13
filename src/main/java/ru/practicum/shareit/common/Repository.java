package ru.practicum.shareit.common;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends AbstractEntity> {
    List<T> getAll();

    Optional<T> getById(long id);

    T create(T entity);

    T update(long id, T entity);

    void delete(long id);
}
