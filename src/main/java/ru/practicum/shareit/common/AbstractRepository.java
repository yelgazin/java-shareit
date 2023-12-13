package ru.practicum.shareit.common;

import java.util.*;

public abstract class AbstractRepository<T extends AbstractEntity> implements Repository<T> {

    protected final Map<Long, T> storage = new HashMap<>();
    private long nextId = 0;

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<T> getById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public T create(T entity) {
        long id = generateNextId();
        entity.setId(id);
        storage.put(id, entity);
        return entity;
    }

    public T update(long id, T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    public void delete(long id) {
        storage.remove(id);
    }

    private long generateNextId() {
        return ++nextId;
    }
}
